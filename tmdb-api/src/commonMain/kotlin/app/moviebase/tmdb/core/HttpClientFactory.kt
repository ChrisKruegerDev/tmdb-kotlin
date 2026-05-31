package app.moviebase.tmdb.core

import app.moviebase.tmdb.TmdbClientConfig
import app.moviebase.tmdb.TmdbVersion
import app.moviebase.tmdb.TmdbWebConfig
import app.moviebase.tmdb.model.TmdbErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

internal object HttpClientFactory {

    fun buildHttpClient(
        version: TmdbVersion,
        config: TmdbClientConfig,
        useAuthentication: Boolean = false
    ): HttpClient {
        val defaultConfig: HttpClientConfig<*>.() -> Unit = {
            val json = JsonFactory.buildJson()

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = TmdbWebConfig.TMDB_HOST
                    path(version.path + "/")
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            // see https://ktor.io/docs/client-content-encoding.html
            install(ContentEncoding) {
                gzip()
                deflate()
            }

            // see https://ktor.io/docs/auth.html
            if (useAuthentication) {
                install(Auth) {
                    bearer {
                        // TMDB doesn't have a refresh token
                        loadTokens {
                            config.tmdbAuthCredentials?.accessTokenProvider?.invoke()?.let {
                                BearerTokens(it, "")
                            }
                        }

                        sendWithoutRequest { request ->
                            request.url.host == TmdbWebConfig.TMDB_HOST
                        }
                    }
                }
            }

            // see https://ktor.io/docs/response-validation.html
            // Keep Ktor's built-in validation disabled so all errors surface as a single TmdbException,
            // including non-2xx responses whose body isn't a TMDB-shaped error (e.g. a CDN/gateway page).
            expectSuccess = config.expectSuccess
            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status.isSuccess()) return@validateResponse

                    // Fall back to the HTTP status when the body isn't a TMDB-shaped error
                    // so every non-2xx response still surfaces as a TmdbException.
                    val tmdbResponse = json.decodeTmdbErrorResponse(response)
                        ?: TmdbErrorResponse(
                            statusCode = response.status.value,
                            statusMessage = response.status.description,
                            success = false,
                        )
                    throw TmdbException(tmdbResponse, requestUrl = response.call.request.url.toString())
                }
            }

            // see https://ktor.io/docs/client-retry.html
            config.maxRequestRetries?.takeIf { it > 0 }?.let { maxRetries ->
                install(HttpRequestRetry) {
                    retryIf(maxRetries) { _, response ->
                        response.status.value in 500..599 ||
                            response.status == HttpStatusCode.TooManyRequests
                    }

                    retryOnExceptionIf(maxRetries) { _, cause ->
                        cause !is CancellationException && cause.isRetryableException()
                    }

                    exponentialDelay(
                        maxDelayMs = 30_000,
                        respectRetryAfterHeader = true,
                    )
                }
            }

            // see https://ktor.io/docs/client-caching.html
            if (config.useCache) {
                install(HttpCache)
            }

            if (config.useTimeout) {
                install(HttpTimeout) {
                    connectTimeoutMillis = 10_000   // host reachability — fail fast
                    socketTimeoutMillis  = 30_000   // stall detection mid-response
                    requestTimeoutMillis = 30_000   // total ceiling per attempt
                }
            }

            config.httpClientLoggingBlock?.let {
                Logging(it)
            }

            config.httpClientConfigBlock?.invoke(this)
        }

        return config.httpClientBuilder?.invoke()?.config(defaultConfig) ?: HttpClient(defaultConfig)
    }

    private suspend fun Json.decodeTmdbErrorResponse(response: HttpResponse): TmdbErrorResponse? {
        return try {
            val exceptionResponseText = response.bodyAsText()
            decodeFromString(TmdbErrorResponse.serializer(), exceptionResponseText)
        } catch (t: Throwable) {
            // if we don't get a TMDB error response, skip the handling
            null
        }
    }

    private fun Throwable.isRetryableException(): Boolean {
        val exception = unwrapCancellationException()
        return exception is HttpRequestTimeoutException ||
            exception is ConnectTimeoutException ||
            exception is SocketTimeoutException ||
            exception is IOException
    }
}
