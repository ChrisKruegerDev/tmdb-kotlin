package app.moviebase.tmdb

import app.moviebase.tmdb.core.TmdbDsl
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.logging.*

@TmdbDsl
class TmdbClientConfig {

    var tmdbApiKey: String? = null
    internal var tmdbAuthCredentials: TmdbAuthCredentials? = null

    var expectSuccess: Boolean = true
    var useCache: Boolean = true
    var useTimeout: Boolean = true
    var maxRequestRetries: Int? = 5

    internal var httpClientConfigBlock: (HttpClientConfig<*>.() -> Unit)? = null
    internal var httpClientBuilder: (() -> HttpClient)? = null
    internal var httpClientLoggingBlock: (LoggingConfig.() -> Unit)? = null

    fun userAuthentication(block: TmdbAuthCredentials.() -> Unit) {
        tmdbAuthCredentials = TmdbAuthCredentials().apply(block)
    }

    fun logging(block: LoggingConfig.() -> Unit) {
        httpClientLoggingBlock = block
    }

    /**
     * Set custom HttpClient configuration for the default HttpClient.
     */
    fun httpClient(block: HttpClientConfig<*>.() -> Unit) {
        this.httpClientConfigBlock = block
    }

    /**
     * Creates an custom [HttpClient] with the specified [HttpClientEngineFactory] and optional [block] configuration.
     * Note that the TMDB config will be added afterwards.
     */
    fun <T : HttpClientEngineConfig> httpClient(
        engineFactory: HttpClientEngineFactory<T>,
        block: HttpClientConfig<T>.() -> Unit = {}
    ) {
        httpClientBuilder = {
            HttpClient(engineFactory, block)
        }
    }

    companion object {

        internal fun withKey(tmdbApiKey: String) = TmdbClientConfig().apply {
            this.tmdbApiKey = tmdbApiKey
        }
    }
}

@TmdbDsl
class TmdbAuthCredentials {

    // used in version 4
    var authenticationToken: String? = null

    internal var sessionIdProvider: (() -> String?)? = null
    internal var guestSessionIdProvider: (() -> String?)? = null
    internal var accessTokenProvider: (() -> String?)? = null

    fun loadSessionId(provider: () -> String?) {
        sessionIdProvider = provider
    }

    fun loadGuestSessionId(provider: () -> String?) {
        guestSessionIdProvider = provider
    }

    fun loadAccessToken(provider: () -> String?) {
        accessTokenProvider = provider
    }
}
