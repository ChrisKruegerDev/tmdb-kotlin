package app.moviebase.tmdb.discover

import app.moviebase.tmdb.model.DiscoverParam
import app.moviebase.tmdb.model.TmdbDiscover
import app.moviebase.tmdb.model.TmdbDiscoverFilter
import app.moviebase.tmdb.model.TmdbDiscoverSeparator
import app.moviebase.tmdb.model.TmdbMediaType
import app.moviebase.tmdb.model.TmdbNetworkId
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DiscoverFactoryTest {

    private val watchProviders = TmdbDiscoverFilter(items = setOf(8, 337))

    @Nested
    inner class `creating a popular discover` {

        @Test
        fun `it applies watch providers and region`() {
            val discover = DiscoverFactory.createPopular(
                mediaType = TmdbMediaType.MOVIE,
                watchProviders = watchProviders,
                watchRegion = "US",
            )

            val parameters = discover.buildParameters()

            assertThat(parameters[DiscoverParam.WITH_WATCH_PROVIDERS]).isEqualTo("8,337")
            assertThat(parameters[DiscoverParam.WATCH_REGION]).isEqualTo("US")
        }

        @Test
        fun `it has no date bound without years`() {
            val discover = DiscoverFactory.createPopular(TmdbMediaType.MOVIE)

            val parameters = discover.buildParameters()

            assertThat(parameters[DiscoverParam.Movie.RELEASE_DATE_GTE]).isNull()
            assertThat(parameters[DiscoverParam.Movie.RELEASE_DATE_LTE]).isNull()
        }

        @Test
        fun `it bounds movies by release date`() {
            val discover = DiscoverFactory.createPopular(TmdbMediaType.MOVIE, withinYears = 1)

            val parameters = discover.buildParameters()

            assertThat(parameters[DiscoverParam.Movie.RELEASE_DATE_GTE]).isNotNull()
            assertThat(parameters[DiscoverParam.Movie.RELEASE_DATE_LTE]).isNotNull()
        }

        @Test
        fun `it bounds shows by air date`() {
            val discover = DiscoverFactory.createPopular(TmdbMediaType.SHOW, withinYears = 1) as TmdbDiscover.Show

            assertThat(discover.airDateGte).isNotNull()
            assertThat(discover.airDateLte).isNotNull()
        }
    }

    @Nested
    inner class `creating a genre discover` {

        @Test
        fun `it filters movies by genre`() {
            val discover = DiscoverFactory.createGenre(
                mediaType = TmdbMediaType.MOVIE,
                genres = TmdbDiscoverFilter(items = listOf("28")),
            )

            val parameters = discover.buildParameters()

            assertThat(parameters[DiscoverParam.WITH_GENRES]).isEqualTo("28")
        }

        @Test
        fun `it filters shows by genre`() {
            val discover = DiscoverFactory.createGenre(
                mediaType = TmdbMediaType.SHOW,
                genres = TmdbDiscoverFilter(items = listOf("10759")),
            )

            val parameters = discover.buildParameters()

            assertThat(parameters[DiscoverParam.WITH_GENRES]).isEqualTo("10759")
        }
    }

    @Nested
    inner class `creating a network discover` {

        @Test
        fun `it joins several networks`() {
            val discover = DiscoverFactory.createNetworks(
                networks = TmdbDiscoverFilter(
                    separator = TmdbDiscoverSeparator.OR,
                    items = listOf(TmdbNetworkId.NETFLIX, TmdbNetworkId.APPLE_TV),
                ),
            )

            val parameters = discover.buildParameters()

            assertThat(parameters[DiscoverParam.Show.WITH_NETWORKS])
                .isEqualTo("${TmdbNetworkId.NETFLIX}|${TmdbNetworkId.APPLE_TV}")
        }

        @Test
        fun `it keeps a single network`() {
            val discover = DiscoverFactory.createNetwork(TmdbNetworkId.NETFLIX)

            val parameters = discover.buildParameters()

            assertThat(parameters[DiscoverParam.Show.WITH_NETWORKS]).isEqualTo(TmdbNetworkId.NETFLIX.toString())
        }
    }

    @Nested
    inner class `creating an airing discover` {

        @Test
        fun `it looks back for currently airing shows`() {
            val discover = DiscoverFactory.createCurrentlyAiring()

            assertThat(discover.airDateGte).isLessThan(discover.airDateLte)
        }

        @Test
        fun `it resolves the category`() {
            val discover = DiscoverFactory.createByCategory(DiscoverCategory.CurrentlyAiring)

            assertThat(discover).isInstanceOf(TmdbDiscover.Show::class.java)
        }
    }
}
