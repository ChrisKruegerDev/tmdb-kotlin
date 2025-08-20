package app.moviebase.tmdb.model

import app.moviebase.tmdb.core.TmdbInstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbReviewDetail(
    @SerialName("id") val id: String,
    @SerialName("author") val author: String,
    @SerialName("author_details") val authorDetails: TmdbReviewAuthor? = null,
    @SerialName("content") val content: String,
    @SerialName("created_at") @Serializable(TmdbInstantSerializer::class) val createdAt: Instant? = null,
    @SerialName("updated_at") @Serializable(TmdbInstantSerializer::class) val updatedAt: Instant? = null,
    @SerialName("url") val url: String
)

@Serializable
data class TmdbReviewAuthor(
    @SerialName("name") val name: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("avatar_path") val avatarPath: String? = null,
    @SerialName("rating") val rating: Float? = null
)
