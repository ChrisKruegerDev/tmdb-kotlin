package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbConfiguration(
    @SerialName("images") val images: TmdbImagesConfiguration,
    @SerialName("change_keys") val changeKeys: List<String> = emptyList()
)

@Serializable
data class TmdbImagesConfiguration(
    @SerialName("base_url") val baseUrl: String,
    @SerialName("secure_base_url") val secureBaseUrl: String,
    @SerialName("backdrop_sizes") val backdropSizes: List<String> = emptyList(),
    @SerialName("poster_sizes") val posterSizes: List<String> = emptyList(),
    @SerialName("logo_sizes") val logoSizes: List<String> = emptyList(),
    @SerialName("profile_sizes") val profileSizes: List<String> = emptyList(),
    @SerialName("still_sizes") val stillSizes: List<String> = emptyList()
)

@Serializable
data class TmdbConfigurationCountry(
    @SerialName("iso_3166_1") val iso3166: String,
    @SerialName("english_name") val englishName: String,
    @SerialName("native_name") val nativeName: String,
)

@Serializable
data class TmdbConfigurationJob(
    @SerialName("department") val department: String,
    @SerialName("jobs") val jobs: List<String>
)

@Serializable
data class TmdbLanguage(
    @SerialName("iso_639_1") val iso639: String,
    @SerialName("english_name") val englishName: String,
    @SerialName("name") val name: String
)

@Serializable
data class TmdbTimezone(
    @SerialName("iso_3166_1") val iso3166: String,
    @SerialName("zones") val zones: List<String>
)
