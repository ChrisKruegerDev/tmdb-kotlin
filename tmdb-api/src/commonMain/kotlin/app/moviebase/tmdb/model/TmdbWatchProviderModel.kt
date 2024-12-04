package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object TmdbWatchProviderId {

    object Flatrate {
        const val NETFLIX = 8
        const val AMAZON_PRIME_VIDEO_TIER_A = 9
        const val AMAZON_PRIME_VIDEO_TIER_B = 119
        const val APPLE_TV_PLUS = 350
        const val DISNEY_PLUS = 337
        const val HBO_MAX = 1899
        const val HULU = 15
        const val PARAMOUNT = 531
    }

    object Buy {
        const val APPLE_ITUNES = 2
        const val GOOGLE_PLAY = 3
        const val AMAZON_VIDEO = 10
        const val MICROSOFT_STORE = 68
    }
}

@Serializable
enum class TmdbWatchMonetizationType(val value: String) {
    @SerialName("flatrate")
    FLATRATE("flatrate"),

    @SerialName("free")
    FREE("free"),

    @SerialName("ads")
    ADS("ads"),

    @SerialName("rent")
    RENT("rent"),

    @SerialName("buy")
    BUY("buy")
}

@Serializable
data class TmdbWatchProviderResult(
    val id: Int? = null,
    val results: Map<String, TmdbProviders>
)

@Serializable
data class TmdbProviders(
    val link: String,
    val flatrate: List<TmdbProvider> = emptyList(),
    val buy: List<TmdbProvider> = emptyList()
)

@Serializable
data class TmdbProvider(
    @SerialName("display_priority") val displayPriority: Int?,
    @SerialName("logo_path") val logoPath: String,
    @SerialName("provider_id") val providerId: Int,
    @SerialName("provider_name") val providerName: String
)
