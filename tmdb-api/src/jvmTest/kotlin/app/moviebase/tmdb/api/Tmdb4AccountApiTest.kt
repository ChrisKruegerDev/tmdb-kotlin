package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlin.time.Instant
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.toInstant
import org.junit.jupiter.api.Test

class Tmdb4AccountApiTest {

    val client = mockHttpClient(
        version = 4,
        responses = mapOf(
            "account/343563/lists?page=1" to "account/account_lists.json"
        )
    )

    val classToTest = Tmdb4AccountApi(client)

    @Test
    fun `it can get account user lists`() = runTest {
        val results = classToTest.getLists("343563", 1)

        assertThat(results).isNotNull()
        assertThat(results.page).isEqualTo(1)
        assertThat(results.totalResults).isEqualTo(20)
        assertThat(results.results).isNotEmpty()

        val result = results.results.first()
        assertThat(result.id).isEqualTo(44676)
        assertThat(result.updatedAt).isEqualTo(Instant.parse("2021-11-20T19:26:52Z"))
        assertThat(result.createdAt).isEqualTo(Instant.parse("2018-01-08T18:03:08Z"))
    }
}
