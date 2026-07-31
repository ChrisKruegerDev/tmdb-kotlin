package app.moviebase.tmdb.discover

import app.moviebase.tmdb.model.TmdbDiscoverFilter
import app.moviebase.tmdb.model.TmdbMediaType
import app.moviebase.tmdb.model.TmdbNetworkId
import app.moviebase.tmdb.model.TmdbWatchProviderId

sealed interface DiscoverCategory {

    data object NowPlaying : DiscoverCategory
    data object Upcoming : DiscoverCategory
    data class Popular(val mediaType: TmdbMediaType) : DiscoverCategory
    data class TopRated(val mediaType: TmdbMediaType) : DiscoverCategory
    data object AiringToday : DiscoverCategory
    data object OnTv : DiscoverCategory
    data object CurrentlyAiring : DiscoverCategory
    data class OnDvd(val mediaType: TmdbMediaType) : DiscoverCategory

    data class Genre(
        val mediaType: TmdbMediaType,
        val genres: TmdbDiscoverFilter<String>,
    ) : DiscoverCategory

    data class Networks(val networks: TmdbDiscoverFilter<Int>) : DiscoverCategory

    data class Network(val network: Int) : DiscoverCategory {
        companion object {
            val NETFLIX = Network(TmdbNetworkId.NETFLIX)
            val AMAZON = Network(TmdbNetworkId.AMAZON)
            val DISNEY_PLUS = Network(TmdbNetworkId.DISNEY_PLUS)
            val APPLE_TV = Network(TmdbNetworkId.APPLE_TV)
        }
    }

    data class OnStreaming(
        val mediaType: TmdbMediaType,
        val watchRegion: String,
        val watchProviders: TmdbDiscoverFilter<Int>
    ) : DiscoverCategory {
        companion object {
            fun Netflix(mediaType: TmdbMediaType, watchRegion: String) = OnStreaming(
                mediaType,
                watchRegion,
                TmdbDiscoverFilter(items = listOf(TmdbWatchProviderId.Flatrate.NETFLIX)),
            )

            fun AmazonPrimeVideo(mediaType: TmdbMediaType, watchRegion: String) = OnStreaming(
                mediaType,
                watchRegion,
                TmdbDiscoverFilter(
                    items = listOf(
                        TmdbWatchProviderId.Flatrate.AMAZON_PRIME_VIDEO_TIER_A,
                        TmdbWatchProviderId.Flatrate.AMAZON_PRIME_VIDEO_TIER_B,
                    ),
                ),
            )

            fun AppleTv(mediaType: TmdbMediaType, watchRegion: String) = OnStreaming(
                mediaType,
                watchRegion,
                TmdbDiscoverFilter(items = listOf(TmdbWatchProviderId.Flatrate.APPLE_TV_PLUS)),
            )

            fun DisneyPlus(mediaType: TmdbMediaType, watchRegion: String) = OnStreaming(
                mediaType,
                watchRegion,
                TmdbDiscoverFilter(items = listOf(TmdbWatchProviderId.Flatrate.DISNEY_PLUS)),
            )
        }
    }
}
