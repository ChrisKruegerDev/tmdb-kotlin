package app.moviebase.tmdb.discover

import app.moviebase.tmdb.core.currentLocalDate
import app.moviebase.tmdb.core.minusWeeks
import app.moviebase.tmdb.core.minusYears
import app.moviebase.tmdb.core.plusDays
import app.moviebase.tmdb.core.plusWeeks
import app.moviebase.tmdb.core.plusYears
import app.moviebase.tmdb.model.TmdbDiscover
import app.moviebase.tmdb.model.TmdbDiscoverFilter
import app.moviebase.tmdb.model.TmdbDiscoverMovieSortBy
import app.moviebase.tmdb.model.TmdbDiscoverShowSortBy
import app.moviebase.tmdb.model.TmdbDiscoverTimeRange
import app.moviebase.tmdb.model.TmdbMediaType
import app.moviebase.tmdb.model.TmdbReleaseType

object DiscoverFactory {

    private const val MIN_VOTE_COUNT = 200

    fun createByCategory(category: DiscoverCategory): TmdbDiscover {
        return when (category) {
            DiscoverCategory.NowPlaying -> createNowPlaying()
            DiscoverCategory.Upcoming -> createUpcoming()
            is DiscoverCategory.Popular -> createPopular(category.mediaType)
            is DiscoverCategory.TopRated -> createTopRated(category.mediaType)
            DiscoverCategory.AiringToday -> createAiringToday()
            DiscoverCategory.OnTv -> createOnTv()
            DiscoverCategory.CurrentlyAiring -> createCurrentlyAiring()
            is DiscoverCategory.OnDvd -> createOnDvd()
            is DiscoverCategory.Genre -> createGenre(category.mediaType, category.genres)
            is DiscoverCategory.Network -> createNetwork(category.network)
            is DiscoverCategory.Networks -> createNetworks(category.networks)
            is DiscoverCategory.OnStreaming -> createOnStreaming(
                category.mediaType,
                category.watchProviders,
                category.watchRegion,
            )
        }
    }

    fun createNowPlaying(
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover.Movie {
        val localDate = currentLocalDate()
        val firstDate = localDate.minusWeeks(6)
        val lastDate = localDate.plusDays(1)

        val discoverTimeRange = TmdbDiscoverTimeRange.Custom(
            firstDate = firstDate.toString(),
            lastDate = lastDate.toString(),
        )

        return TmdbDiscover.Movie(
            releaseDate = discoverTimeRange,
            withReleaseTypes = TmdbDiscoverFilter(items = setOf(TmdbReleaseType.THEATRICAL)),
            withWatchProviders = watchProviders,
            watchRegion = watchRegion,
        )
    }

    fun createAiringToday(
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover.Show {
        val localDate = currentLocalDate().toString()

        return TmdbDiscover.Show(
            airDateGte = localDate,
            airDateLte = localDate,
            withWatchProviders = watchProviders,
            watchRegion = watchRegion,
        )
    }

    fun createOnTv(
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover.Show {
        val airDateGte = currentLocalDate()
        val airDateLte = airDateGte.plusWeeks(2)

        return TmdbDiscover.Show(
            airDateGte = airDateGte.toString(),
            airDateLte = airDateLte.toString(),
            withWatchProviders = watchProviders,
            watchRegion = watchRegion,
        )
    }

    /**
     * Shows with an episode in the recent past, unlike [createOnTv], which looks ahead.
     *
     * e. g. discover/tv?page=1&air_date.gte=2026-07-03&air_date.lte=2026-07-10&sort_by=popularity.desc
     */
    fun createCurrentlyAiring(
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover.Show {
        val airDateGte = currentLocalDate().minusWeeks(4)
        val airDateLte = airDateGte.plusWeeks(1)

        return TmdbDiscover.Show(
            sortBy = TmdbDiscoverShowSortBy.POPULARITY,
            airDateGte = airDateGte.toString(),
            airDateLte = airDateLte.toString(),
            withWatchProviders = watchProviders,
            watchRegion = watchRegion,
        )
    }

    /**
     * e. g. discover/movie?page=1&sort_by=release_date.desc&with_release_type=5
     */
    fun createOnDvd(
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover =
        TmdbDiscover.Movie(
            sortBy = TmdbDiscoverMovieSortBy.RELEASE_DATE,
            withReleaseTypes = TmdbDiscoverFilter(items = setOf(TmdbReleaseType.PHYSICAL)),
            withWatchProviders = watchProviders,
            watchRegion = watchRegion,
        )

    /**
     * e. g. discover/movie?page=1&release_date.lte=2021-05-08&language=de&sort_by=popularity.desc&region=DE&release_date.gte=2021-04-19
     */
    fun createUpcoming(
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover {
        val localDate = currentLocalDate()
        val firstDate = localDate.plusDays(2)
        val lastDate = localDate.plusWeeks(3)

        val discoverTimeRange = TmdbDiscoverTimeRange.Custom(
            firstDate = firstDate.toString(),
            lastDate = lastDate.toString(),
        )

        return TmdbDiscover.Movie(
            releaseDate = discoverTimeRange,
            withWatchProviders = watchProviders,
            watchRegion = watchRegion,
        )
    }

    /**
     * [withinYears] bounds the result to titles released or aired in that many years around today.
     * Popularity alone keeps returning long-tail catalog titles, which rarely suits a "popular now" row.
     */
    fun createPopular(
        mediaType: TmdbMediaType,
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
        withinYears: Int? = null,
    ): TmdbDiscover {
        val localDate = currentLocalDate()
        val firstDate = withinYears?.let { localDate.minusYears(it).toString() }
        val lastDate = withinYears?.let { localDate.plusYears(it).toString() }

        return when (mediaType) {
            TmdbMediaType.MOVIE -> TmdbDiscover.Movie(
                sortBy = TmdbDiscoverMovieSortBy.POPULARITY,
                releaseDate = withinYears?.let { TmdbDiscoverTimeRange.Custom(firstDate, lastDate) },
                withWatchProviders = watchProviders,
                watchRegion = watchRegion,
            )

            TmdbMediaType.SHOW -> TmdbDiscover.Show(
                sortBy = TmdbDiscoverShowSortBy.POPULARITY,
                airDateGte = firstDate,
                airDateLte = lastDate,
                withWatchProviders = watchProviders,
                watchRegion = watchRegion,
            )

            else -> throw IllegalArgumentException("$mediaType type is not supported for discover")
        }
    }

    fun createTopRated(
        mediaType: TmdbMediaType,
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover {
        return when (mediaType) {
            TmdbMediaType.MOVIE -> TmdbDiscover.Movie(
                sortBy = TmdbDiscoverMovieSortBy.VOTE_AVERAGE,
                voteCountGte = MIN_VOTE_COUNT,
                withWatchProviders = watchProviders,
                watchRegion = watchRegion,
            )

            TmdbMediaType.SHOW -> TmdbDiscover.Show(
                sortBy = TmdbDiscoverShowSortBy.VOTE_AVERAGE,
                voteCountGte = MIN_VOTE_COUNT,
                withWatchProviders = watchProviders,
                watchRegion = watchRegion,
            )

            else -> throw IllegalArgumentException("$mediaType type is not supported for discover")
        }
    }

    /**
     * e. g. discover/movie?page=1&with_genres=28&sort_by=popularity.desc
     */
    fun createGenre(
        mediaType: TmdbMediaType,
        genres: TmdbDiscoverFilter<String>,
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover {
        return when (mediaType) {
            TmdbMediaType.MOVIE -> TmdbDiscover.Movie(
                sortBy = TmdbDiscoverMovieSortBy.POPULARITY,
                withGenres = genres,
                withWatchProviders = watchProviders,
                watchRegion = watchRegion,
            )

            TmdbMediaType.SHOW -> TmdbDiscover.Show(
                sortBy = TmdbDiscoverShowSortBy.POPULARITY,
                withGenres = genres,
                withWatchProviders = watchProviders,
                watchRegion = watchRegion,
            )

            else -> throw IllegalArgumentException("$mediaType type is not supported for discover")
        }
    }

    /**
     * e. g. discover/tv?page=1&with_networks=213&language=de&sort_by=popularity.desc&region=DE
     */
    fun createNetwork(
        network: Int,
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover.Show = createNetworks(
        networks = TmdbDiscoverFilter(items = setOf(network)),
        watchProviders = watchProviders,
        watchRegion = watchRegion,
    )

    /**
     * e. g. discover/tv?page=1&with_networks=213|1024&sort_by=popularity.desc
     */
    fun createNetworks(
        networks: TmdbDiscoverFilter<Int>,
        watchProviders: TmdbDiscoverFilter<Int>? = null,
        watchRegion: String? = null,
    ): TmdbDiscover.Show {
        return TmdbDiscover.Show(
            withNetworks = networks,
            sortBy = TmdbDiscoverShowSortBy.POPULARITY,
            withWatchProviders = watchProviders,
            watchRegion = watchRegion,
        )
    }

    fun createOnStreaming(mediaType: TmdbMediaType, watchProviders: TmdbDiscoverFilter<Int>, watchRegion: String): TmdbDiscover {
        return when (mediaType) {
            TmdbMediaType.MOVIE -> TmdbDiscover.Movie(
                sortBy = TmdbDiscoverMovieSortBy.POPULARITY,
                withWatchProviders = watchProviders,
                watchRegion = watchRegion,
            )

            TmdbMediaType.SHOW -> TmdbDiscover.Show(
                sortBy = TmdbDiscoverShowSortBy.POPULARITY,
                withWatchProviders = watchProviders,
                watchRegion = watchRegion,
            )

            else -> throw IllegalArgumentException("$mediaType type is not supported for discover")
        }
    }

    fun createForOneYear(mediaType: TmdbMediaType): TmdbDiscover {
        val discoverTimeRange = TmdbDiscoverTimeRange.OneYear(
            year = currentLocalDate().year,
        )

        return when (mediaType) {
            TmdbMediaType.MOVIE -> TmdbDiscover.Movie(releaseDate = discoverTimeRange)
            TmdbMediaType.SHOW -> TmdbDiscover.Show(firstAirDate = discoverTimeRange)
            else -> throw IllegalArgumentException("$mediaType type is not supported for discover")
        }
    }
}
