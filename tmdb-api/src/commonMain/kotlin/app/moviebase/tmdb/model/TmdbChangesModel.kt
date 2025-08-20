package app.moviebase.tmdb.model

import app.moviebase.tmdb.core.TmdbInstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbChange(
    @SerialName("key") val key: String,
    @SerialName("items") val items: List<TmdbChangeItem>
)

@Serializable
data class TmdbChangeItem(
    @SerialName("id") val id: String,
    @SerialName("action") val action: String,
    @SerialName("time") @Serializable(TmdbInstantSerializer::class) val time: Instant,
    @SerialName("iso_639_1") val iso639: String? = null,
    @SerialName("iso_3166_1") val iso3166: String? = null,
    @SerialName("value") val value: TmdbChangeValue? = null,
    @SerialName("original_value") val originalValue: TmdbChangeValue? = null
)

@Serializable
data class TmdbChangeValue(
    @SerialName("value") val value: String? = null
)

@Serializable
data class TmdbChangesResponse(
    @SerialName("changes") val changes: List<TmdbChange>
)

@Serializable
data class TmdbChangedMedia(
    @SerialName("id") val id: Int,
    @SerialName("adult") val adult: Boolean? = null
)

@Serializable
data class TmdbChangedMoviesResponse(
    @SerialName("results") val results: List<TmdbChangedMedia>,
    @SerialName("page") val page: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class TmdbChangedTvResponse(
    @SerialName("results") val results: List<TmdbChangedMedia>,
    @SerialName("page") val page: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class TmdbChangedPeopleResponse(
    @SerialName("results") val results: List<TmdbChangedMedia>,
    @SerialName("page") val page: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)
