package app.moviebase.tmdb.model

import app.moviebase.tmdb.image.TmdbImage
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TmdbMediaType(val value: String) {
    @SerialName("movie")
    MOVIE("movie"),

    @SerialName("tv")
    SHOW("tv"),

    @SerialName("season")
    SEASON("season"),

    @SerialName("episode")
    EPISODE("episode")
}

interface TmdbAnyItem {
    val id: Int
}

interface TmdbBackdropItem {
    val backdropPath: String?
    val backdropImage get(): TmdbImage? = TmdbImage.backdrop(backdropPath)
}

interface TmdbPosterItem {
    val posterPath: String?
    val posterImage get(): TmdbImage? = TmdbImage.poster(posterPath)
}

interface TmdbProfileItem {
    val profilePath: String?
    val profileImage get(): TmdbImage? = TmdbImage.profile(profilePath)
}

interface TmdbRatingItem {
    val voteAverage: Float?
    val voteCount: Int?
}

/**
 * This item is a movie or show which can be listed.
 */
@Polymorphic
@Serializable
sealed interface TmdbMediaListItem : TmdbAnyItem, TmdbBackdropItem, TmdbPosterItem, TmdbRatingItem {
    val overview: String?
    val genresIds: List<Int>
    val popularity: Float
    val originalLanguage: String
}
