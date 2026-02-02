package app.moviebase.tmdb.model

import kotlinx.datetime.LocalDate
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

@Serializable
data class TmdbCollectionDetail(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("original_language") val originalLanguage: String?,
    @SerialName("original_name") val originalName: String?,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("parts") val parts: List<TmdbCollectionPart>,
    @SerialName("images") val images: TmdbImages? = null,
    @SerialName("translations") val translations: TmdbCollectionTranslations? = null,
)

@Serializable
data class TmdbCollectionPart(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("media_type") val mediaType: TmdbMediaType,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("genre_ids") val genreIds: List<Int>,
    @SerialName("popularity") val popularity: Float = 0f,
    @SerialName("release_date") val releaseDate: LocalDate?,
    @SerialName("video") val video: Boolean = false,
    @SerialName("vote_average") override val voteAverage: Float = 0f,
    @SerialName("vote_count") override val voteCount: Int = 0,
): TmdbRatingItem

typealias TmdbCollectionTranslations = TmdbTranslations<TmdbCollectionTranslationData>

@Serializable
data class TmdbCollectionTranslationData(
    val title: String,
    val overview: String,
    val homepage: String
)
