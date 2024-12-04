package app.moviebase.tmdb

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.Properties

/**
 * Save properties in your the file local.properties in the root project.
 */
val properties by lazy {
    Properties().apply {
        val parent = Paths.get(System.getProperty("user.dir")).parent
        FileInputStream("$parent/local.properties").use {
            load(it)
        }
    }
}

fun createTmdbSessionCredentials() = TmdbSessionCredentials(
    userName = properties.getProperty("TMDB_USER_NAME"),
    password = properties.getProperty("TMDB_USER_PASSWORD"),
    approvedRequestToken = properties.getProperty("TMDB_APPROVED_REQUEST_TOKEN"),
    sessionId = properties.getProperty("TMDB_SESSION_ID"),
    approvedRequestTokenVersion4 = properties.getProperty("TMDB4_APPROVED_REQUEST_TOKEN"),
    accessTokenVersion4 = properties.getProperty("TMDB4_ACCESS_TOKEN"),
    accountId4 = properties.getProperty("TMDB4_ACCOUNT_ID")
)

fun defaultTmdbConfiguration(
    apiKey: String? = null,
    storage: TmdbAccountStorage? = null
): TmdbClientConfig.() -> Unit = {
    tmdbApiKey = apiKey ?: properties.getProperty("TMDB_API_KEY")

    userAuthentication {
        authenticationToken = properties.getProperty("TMDB_AUTHENTICATION_TOKEN")

        loadSessionId { storage?.sessionId }
        loadAccessToken { storage?.accessToken }
    }

    useCache = true
    useTimeout = true
    maxRequestRetries = 3

    httpClient(OkHttp) {
        logging {
            logger = TestLogger()
            level = LogLevel.HEADERS
        }
    }
}

fun buildTmdb4(
    apiKey: String? = null,
    tmdbAccountStorage: TmdbAccountStorage? = null
): Tmdb4 = Tmdb4(defaultTmdbConfiguration(apiKey, tmdbAccountStorage))

fun buildTmdb3(
    apiKey: String? = null,
    tmdbAccountStorage: TmdbAccountStorage? = null
): Tmdb3 = Tmdb3(defaultTmdbConfiguration(apiKey, tmdbAccountStorage))

class TestLogger : Logger {

    override fun log(message: String) {
        println("HttpClient: $message")
    }
}
