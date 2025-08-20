package app.moviebase.tmdb.model

import app.moviebase.tmdb.core.TmdbInstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tmdb4ListMeta(
    @SerialName("iso_639_1") val iso639: String,
    val id: Int,
    @SerialName("featured") val featuredInt: Int,
    val description: String? = null,
    val revenue: String,
    @SerialName("public") val publicInt: Int,
    val name: String,
    @SerialName("updated_at")
    @Serializable(TmdbInstantSerializer::class)
    val updatedAt: Instant?,
    @SerialName("created_at")
    @Serializable(TmdbInstantSerializer::class)
    val createdAt: Instant?,
    @SerialName("sort_by") val sortBy: Int,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("average_rating") val averageRating: Float? = null,
    @SerialName("iso_3166_1") val iso3166: String? = null,
    @SerialName("adult") val adultInt: Int,
    @SerialName("number_of_items") val numberOfItems: Int,
    @SerialName("poster_path") val posterPath: String? = null
) {

    val featured: Boolean get() = featuredInt == 1
    val public: Boolean get() = publicInt == 1
    val adult: Boolean get() = adultInt == 1
}

@Serializable
data class TmdbListMetaPageResult(
    @SerialName("page") override val page: Int,
    @SerialName("results") override val results: List<Tmdb4ListMeta> = emptyList(),
    @SerialName("total_results") override val totalResults: Int,
    @SerialName("total_pages") override val totalPages: Int
) : TmdbPageResult<Tmdb4ListMeta>
