package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbCertification(
    @SerialName("certification") val certification: String,
    @SerialName("meaning") val meaning: String,
    @SerialName("order") val order: Int
)

@Serializable
data class TmdbCertificationCountry(
    @SerialName("iso_3166_1") val iso3166: String,
    @SerialName("certifications") val certifications: List<TmdbCertification>
)

@Serializable
data class TmdbMovieCertifications(
    @SerialName("certifications") val certifications: Map<String, List<TmdbCertification>>
)

@Serializable
data class TmdbTvCertifications(
    @SerialName("certifications") val certifications: Map<String, List<TmdbCertification>>
)
