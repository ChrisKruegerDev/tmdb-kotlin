package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbKeywordDetail(
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("id") override val id: Int = 0,
    @SerialName("name") val name: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null
) : TmdbSearchable

@Serializable
data class TmdbKeywordPageResult(
    @SerialName("page") override val page: Int,
    @SerialName("results") override val results: List<TmdbKeywordDetail> = emptyList(),
    @SerialName("total_results") override val totalResults: Int,
    @SerialName("total_pages") override val totalPages: Int
) : TmdbPageResult<TmdbKeywordDetail>
