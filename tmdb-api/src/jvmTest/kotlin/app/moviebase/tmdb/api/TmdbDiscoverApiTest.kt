package app.moviebase.tmdb.api

import app.moviebase.tmdb.discover.DiscoverCategory
import app.moviebase.tmdb.model.TmdbDiscover
import app.moviebase.tmdb.model.TmdbDiscoverMovieSortBy
import app.moviebase.tmdb.model.TmdbDiscoverTimeRange
import app.moviebase.tmdb.model.TmdbMediaListItem
import app.moviebase.tmdb.model.TmdbMediaType
import app.moviebase.tmdb.model.TmdbPageResult
import app.moviebase.tmdb.model.TmdbSortOrder
import app.moviebase.tmdb.model.TmdbWatchProviderId
import app.moviebase.tmdb.core.currentLocalDate
import app.moviebase.tmdb.core.mockHttpClient
import app.moviebase.tmdb.core.plusDays
import app.moviebase.tmdb.core.plusWeeks
import app.moviebase.tmdb.model.TmdbDiscoverFilter
import app.moviebase.tmdb.model.TmdbDiscoverSeparator
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TmdbDiscoverApiTest {

    val localDate = currentLocalDate()
    val firstDate = localDate.plusDays(2)
    val lastDate = localDate.plusWeeks(3)

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "discover/movie?page=1&language=de&region=DE&vote_count.gte=200&release_date.lte=2021-12-31&vote_average.gte=5.1&sort_by=popularity.desc&release_date.gte=2020-01-01"
                to "discover/discover_movie.json",
            "discover/movie?page=1&language=de&region=DE&with_release_type=5&sort_by=release_date.desc"
                to "discover/discover_movie_on_dvd.json",
            "discover/movie?page=1&language=de&region=DE&release_date.lte=$lastDate&sort_by=popularity.desc&release_date.gte=$firstDate"
                to "discover/discover_movie_upcoming.json",
            "discover/tv?page=1&language=de&region=DE&with_networks=213&sort_by=popularity.desc"
                to "discover/discover_tv_network_netflix.json",
            "discover/movie?page=1&language=de&region=DE&with_watch_providers=8&watch_region=DE&sort_by=popularity.desc"
                to "discover/discover_movie_watch_provider_netflix_DE.json",
            "discover/tv?page=1&language=de&region=DE&with_watch_providers=8|9|119|337|350&watch_region=DE&sort_by=popularity.desc"
                to "discover/discover_tv_watch_providers.json"
        )
    )

    val classToTest = TmdbDiscoverApi(client)

    @Test
    fun `it can discover by discover class`() = runTest {
        val discover = TmdbDiscover.Movie(
            sortBy = TmdbDiscoverMovieSortBy.POPULARITY,
            sortOrder = TmdbSortOrder.DESC,
            voteAverageGte = 5.1f,
            voteCountGte = 200,
            releaseDate = TmdbDiscoverTimeRange.BetweenYears(from = 2020, to = 2021)
        )

        val result = classToTest.discoverMovie(
            page = 1,
            region = "DE",
            language = "de",
            discover = discover
        )

        assertThat(result.results).isNotEmpty()
        val item = result.results.first()
        assertThat(item.id).isEqualTo(616180)
    }

    @Test
    fun `it can discover movies on dvd`() = runTest {
        val result = classToTest.discoverByCategory(
            page = 1,
            region = "DE",
            language = "de",
            category = DiscoverCategory.OnDvd(TmdbMediaType.MOVIE)
        )

        assertThat(result.page).isEqualTo(1)
        assertThat(result.results).isNotEmpty()
        val item = result.results.first()
        assertThat(item.id).isEqualTo(616180)
        assertThat(result.totalPages).isEqualTo(250)
        assertThat(result.totalResults).isEqualTo(5000)
    }

    @Test
    fun `it can discover upcoming movies`() = runTest {
        val result = classToTest.discoverByCategory(
            page = 1,
            region = "DE",
            language = "de",
            category = DiscoverCategory.Upcoming
        )

        assertThat(result.results).isNotEmpty()
        val item = result.results.first()
        assertThat(item.id).isEqualTo(616180)
    }

    @Test
    fun `it can discover TV shows on a certain network`() = runTest {
        val result = classToTest.discoverByCategory(
            page = 1,
            region = "DE",
            language = "de",
            category = DiscoverCategory.Network.NETFLIX
        )

        assertThat(result.results).isNotEmpty()
        val item = result.results.first()
        assertThat(item.id).isEqualTo(120168)
    }

    @Test
    fun `it can discover movies on streaming provider netflix`() = runTest {
        val discoverCategory = DiscoverCategory.OnStreaming.Netflix(
            mediaType = TmdbMediaType.MOVIE,
            watchRegion = "DE"
        )
        val result = classToTest.discoverByCategory(
            page = 1,
            region = "DE",
            language = "de",
            category = discoverCategory
        )

        assertThat(result.results).isNotEmpty()
        val item = result.results.first()
        assertThat(item.id).isEqualTo(616180)
    }

    @Test
    fun `it can discover shows on streaming providers`() = runTest {
        val result: TmdbPageResult<out TmdbMediaListItem> = classToTest.discoverByCategory(
            page = 1,
            region = "DE",
            language = "de",
            category = DiscoverCategory.OnStreaming(
                TmdbMediaType.SHOW,
                "DE",
                TmdbDiscoverFilter(
                    TmdbDiscoverSeparator.OR,
                    listOf(
                        TmdbWatchProviderId.Flatrate.NETFLIX,
                        TmdbWatchProviderId.Flatrate.AMAZON_PRIME_VIDEO_TIER_A,
                        TmdbWatchProviderId.Flatrate.AMAZON_PRIME_VIDEO_TIER_B,
                        TmdbWatchProviderId.Flatrate.DISNEY_PLUS,
                        TmdbWatchProviderId.Flatrate.APPLE_TV_PLUS
                    )
                )
            )
        )

        assertThat(result.results).isNotEmpty()
        val item = result.results.first()
        assertThat(item.id).isEqualTo(88396)
        assertThat(item.posterPath).isEqualTo("/6kbAMLteGO8yyewYau6bJ683sw7.jpg")
    }
}
