package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object TmdbCompanyId {
    const val NETFLIX_INTERNATIONAL_PICTURES = 145174
    const val WALT_DISNEY_PICTURES = 2
    const val NEW_LINE_CINEMA = 12
    const val CENTURY_STUDIOS = 127928
    const val UNIVERSAL = 33
    const val PARAMOUNT_PICTURES = 107355
    const val WARNER_BROS_PICTURES = 174
    const val COLUMBIA_PICTURES = 5
    const val TRISTAR_PICTURES = 559
}

@Serializable
data class TmdbCompany(
    @SerialName("id") override val id: Int,
    @SerialName("logo_path") val logoPath: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("origin_country") val originCountry: String? = null // e. g. US
) : TmdbSearchable

@Serializable
data class TmdbCompanyPageResult(
    @SerialName("page") override val page: Int,
    @SerialName("results") override val results: List<TmdbCompany> = emptyList(),
    @SerialName("total_results") override val totalResults: Int,
    @SerialName("total_pages") override val totalPages: Int
) : TmdbPageResult<TmdbCompany>

@Serializable
data class TmdbCompanyDetail(
    @SerialName("id") val id: Int = 0,
    @SerialName("headquarters") val headquarters: String? = null,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("logo_path") val logoPath: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("origin_country") val originCountry: String? = null // e. g. US
)

@Serializable
data class TmdbAlternativeName(
    @SerialName("name") val name: String,
    @SerialName("type") val type: String
)

@Serializable
data class TmdbCompanyImages(
    @SerialName("logos") val logos: List<TmdbFileImage>
)
