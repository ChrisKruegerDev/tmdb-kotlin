package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.mockHttpClient
import app.moviebase.tmdb.model.TmdbMovie
import app.moviebase.tmdb.model.TmdbPerson
import app.moviebase.tmdb.model.TmdbShow
import app.moviebase.tmdb.model.TmdbTimeWindow
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TmdbTrendingApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "trending/all/week?language=en&page=1"
                to "trending/trending_all.json",
            "trending/movie/week?language=en&region=US&page=1"
                to "trending/trending_movie.json",
            "trending/tv/week?language=en&region=US&page=1"
                to "trending/trending_tv.json",
            "trending/person/week?language=en&region=US&page=1"
                to "trending/trending_person.json",
        )
    )

    val classToTest = TmdbTrendingApi(client)

    @Test
    fun `it can fetch all trending content for a week`() = runTest {
        val result = classToTest.getTrendingAll(
            page = 1,
            language = "en",
            timeWindow = TmdbTimeWindow.WEEK
        )

        assertThat(result.results).hasSize(3)
        assertThat(result.results[0]).isInstanceOf(TmdbMovie::class.java)
        assertThat(result.results[1]).isInstanceOf(TmdbShow::class.java)
        assertThat(result.results[2]).isInstanceOf(TmdbPerson::class.java)
        assertThat(result.page).isEqualTo(1)
    }

    @Test
    fun `it can fetch trending movies for a week`() = runTest {

        val result = classToTest.getTrendingMovies(
            page = 1,
            region = "US",
            language = "en",
            timeWindow = TmdbTimeWindow.WEEK
        )

        assertThat(result.results).isNotEmpty()
        assertThat(result.page).isEqualTo(1)
    }

    @Test
    fun `it can fetch trending shows for a week`() = runTest {
        val result = classToTest.getTrendingShows(
            page = 1,
            region = "US",
            language = "en",
            timeWindow = TmdbTimeWindow.WEEK
        )

        assertThat(result.results).isNotEmpty()
        assertThat(result.page).isEqualTo(1)
    }

    @Test
    fun `it can fetch trending people for a week`() = runTest {
        val result = classToTest.getTrendingPeople(
            page = 1,
            region = "US",
            language = "en",
            timeWindow = TmdbTimeWindow.WEEK
        )

        assertThat(result.results).isNotEmpty()
        assertThat(result.page).isEqualTo(1)
    }
}
