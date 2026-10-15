package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TmdbWatchProvidersApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "watch/providers/movie?watch_region=IN" to "watch_providers/movie_list_IN.json",
            "watch/providers/tv?watch_region=IN" to "watch_providers/tv_list_IN.json",
            "watch/providers/movie?watch_region=XX" to "watch_providers/error_unauthorized.json",
        )
    )

    val classToTest = TmdbWatchProvidersApi(client)

    @Test
    fun `it can get movie providers for a region`() = runTest {
        val response = classToTest.getMovieList(watchRegion = "IN")

        assertThat(response.results.map { it.providerId }).containsExactly(8, 2336, 232)
        assertThat(response.results[1].providerName).isEqualTo("JioHotstar")
        assertThat(response.results[1].logoPath).isEqualTo("/ledoS6EgdjTNq8F1e6wubUQer18.png")
    }

    @Test
    fun `it can get tv providers for a region`() = runTest {
        val response = classToTest.getTvList(watchRegion = "IN")

        assertThat(response.results.map { it.providerId }).contains(232)
    }

    @Test
    fun `it fails on an error body instead of returning no providers`() = runTest {
        val result = runCatching { classToTest.getMovieList(watchRegion = "XX") }

        assertThat(result.isFailure).isTrue()
    }
}
