package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("collection")
@Serializable
data class TmdbCollection(
    @SerialName("adult") val adult: Boolean,
    @SerialName("id") override val id: Int,
    @SerialName("name") val name: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?
) : TmdbSearchable, TmdbSearchableListItem

@Serializable
data class TmdbCollectionPageResult(
    @SerialName("page") override val page: Int,
    @SerialName("results") override val results: List<TmdbCollection> = emptyList(),
    @SerialName("total_results") override val totalResults: Int,
    @SerialName("total_pages") override val totalPages: Int
) : TmdbPageResult<TmdbCollection>

typealias TmdbCollectionTranslations = TmdbTranslations<TmdbCollectionTranslationData>

@Serializable
data class TmdbCollectionTranslationData(
    val title: String,
    val overview: String,
    val homepage: String
)
