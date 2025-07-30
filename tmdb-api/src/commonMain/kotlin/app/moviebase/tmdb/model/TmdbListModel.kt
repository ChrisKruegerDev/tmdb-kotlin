package app.moviebase.tmdb.model

import app.moviebase.tmdb.core.TmdbInstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class TmdbListSortBy(val value: String) {
    ORIGINAL_TITLE("original_title"),
    VOTE_AVERAGE("vote_average"),
    PRIMARY_RELEASE_DATE("primary_release_date"),
    TITLE("title"),
    POPULARITY("popularity"),
    RELEASE_DATE("release_date"),
    REVENUE("revenue"),
    VOTE_COUNT("vote_count")
}

@Serializable
data class TmdbList(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("favorite_count") val favoriteCount: Int,
    @SerialName("item_count") val itemCount: Int,
    @SerialName("iso_639_1") val iso639: String,
    @SerialName("list_type") val listType: String,
    @SerialName("poster_path") val posterPath: String? = null
)

@Serializable
data class TmdbListDetail(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("favorite_count") val favoriteCount: Int,
    @SerialName("item_count") val itemCount: Int,
    @SerialName("iso_639_1") val iso639: String,
    @SerialName("list_type") val listType: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("created_by") val createdBy: String,
    @SerialName("items") val items: List<TmdbMovie>
)

@Serializable
data class TmdbListCreateResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String,
    @SerialName("list_id") val listId: String? = null
)

@Serializable
data class TmdbListItemStatus(
    @SerialName("id") val id: String,
    @SerialName("item_present") val itemPresent: Boolean
)
