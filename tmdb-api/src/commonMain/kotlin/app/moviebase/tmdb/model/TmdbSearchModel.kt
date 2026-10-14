package app.moviebase.tmdb.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class TmdbSearchType(val value: String) {
    COMPANY("company"),
    COLLECTION("collection"),
    KEYWORD("keyword"),
    PERSON("person"),
    MOVIE("movie"),
    TV("tv"),
    MULTI("multi"),
}

@Serializable
data class TmdbFindResults(
    @SerialName("movie_results") val movieResults: List<TmdbMovie> = emptyList(),
    @SerialName("tv_results") val showResults: List<TmdbShow> = emptyList(),
    @SerialName("person_results") val personResults: List<TmdbPerson> = emptyList(),
    @SerialName("tv_episode_results") val episodeResults: List<TmdbEpisode> = emptyList(),
)

interface TmdbSearchable {
    val id: Int
}

@Polymorphic
@Serializable
sealed interface TmdbSearchableListItem : TmdbAnyItem, TmdbSearchable

@Serializable
data class TmdbMultiPageResult(
    @SerialName("page") override val page: Int,
    @SerialName("results") override val results: List<TmdbSearchableListItem> = emptyList(),
    @SerialName("total_results") override val totalResults: Int,
    @SerialName("total_pages") override val totalPages: Int
) : TmdbPageResult<TmdbSearchableListItem>
