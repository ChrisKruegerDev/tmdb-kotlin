package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlin.time.Instant
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test

class TmdbAuthenticationApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "authentication/token/new" to "authentication/request_token.json"
        )
    )

    val classToTest = TmdbAuthenticationApi(client)

    @Test
    fun `it should return request token`() = runTest {
        val requestToken = classToTest.requestToken()
        val currentDateTime = Instant.parse("2023-03-05T10:38:01Z")

        assertThat(requestToken.success).isTrue()
        // 2007-12-31T23:59:01
        assertThat(requestToken.expiredAt).isEqualTo(currentDateTime)
        assertThat(requestToken.requestToken).isEqualTo("57e299f02ff5309efcdab5b2c26c8ca80aadfce7")
    }

    @Test
    fun `it build the redirect URL`() = runTest {
        val url = classToTest.requestAuthorizationUrl("auth://app")

        assertThat(url).isEqualTo("https://www.themoviedb.org/authenticate/57e299f02ff5309efcdab5b2c26c8ca80aadfce7?redirect_to=auth://app")
    }
}
