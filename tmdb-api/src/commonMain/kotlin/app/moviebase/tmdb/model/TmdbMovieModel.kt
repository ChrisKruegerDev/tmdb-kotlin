package app.moviebase.tmdb.model

import app.moviebase.tmdb.image.TmdbImage
import app.moviebase.tmdb.core.LocalDateSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TmdbReleaseType(val value: Int) {
    @SerialName("1")
    PREMIERE(1),

    @SerialName("2")
    THEATRICAL_LIMITED(2),

    @SerialName("3")
    THEATRICAL(3),

    @SerialName("4")
    DIGITAL(4),

    @SerialName("5")
    PHYSICAL(5),

    @SerialName("6")
    TV(6);

    companion object {
        fun find(value: Int?) = entries.find { it.value == value }
    }
}

@Serializable
enum class TmdbMovieStatus(val value: String) {
    @SerialName("Rumored")
    RUMORED("Rumored"),

    @SerialName("Planned")
    PLANNED("Planned"),

    @SerialName("In Production")
    IN_PRODUCTION("In Production"),

    @SerialName("Post Production")
    POST_PRODUCTION("Post Production"),

    @SerialName("Released")
    RELEASED("Released"),

    @SerialName("Canceled")
    CANCELED("Canceled");

    companion object {
        fun find(value: String?) = entries.find { it.value == value }
    }
}

@Serializable
@SerialName("movie")
data class TmdbMovie(
    @SerialName("poster_path") override val posterPath: String?,
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("overview") override val overview: String,
    @SerialName("release_date")
    @Serializable(LocalDateSerializer::class)
    val releaseDate: LocalDate? = null,
    @SerialName("genre_ids") override val genresIds: List<Int> = emptyList(),
    @SerialName("id") override val id: Int,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") override val originalLanguage: String,
    @SerialName("title") val title: String,
    @SerialName("backdrop_path") override val backdropPath: String?,
    @SerialName("popularity") override val popularity: Float = 0f,
    @SerialName("vote_count") override val voteCount: Int = 0,
    @SerialName("video") val video: Boolean = false,
    @SerialName("vote_average") override val voteAverage: Float = 0f
) : TmdbMediaListItem, TmdbSearchableListItem

@Serializable
data class TmdbMoviePageResult(
    @SerialName("page") override val page: Int,
    @SerialName("results") override val results: List<TmdbMovie> = emptyList(),
    @SerialName("total_results") override val totalResults: Int,
    @SerialName("total_pages") override val totalPages: Int
) : TmdbPageResult<TmdbMovie>


@Serializable
data class TmdbMovieDetail(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("budget") val budget: Long,
    @SerialName("genres") val genres: List<TmdbGenre>,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("id")val id: Int,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("origin_country") val originCountry: List<String> = emptyList(),
    @SerialName("title") val title: String,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("vote_average") override val voteAverage: Float,
    @SerialName("vote_count") override val voteCount: Int,
    @SerialName("external_ids") val externalIds: TmdbExternalIds? = null,
    @SerialName("status") val status: TmdbMovieStatus,
    @SerialName("tagline") val tagline: String,
    @SerialName("video") val video: Boolean,
    @SerialName("popularity") val popularity: Float,
    @SerialName("release_date")
    @Serializable(LocalDateSerializer::class)
    val releaseDate: LocalDate?,
    @SerialName("revenue") val revenue: Long,
    @SerialName("release_dates") val releaseDates: TmdbResult<TmdbReleaseDates>? = null,
    @SerialName("production_companies") val productionCompanies: List<TmdbCompany>? = null,
    @SerialName("production_countries") val productionCountries: List<TmdbCountry>? = null,
    @SerialName("watch/providers") val watchProviders: TmdbWatchProviderResult? = null,
    @SerialName("credits") val credits: TmdbCredits? = null,
    @SerialName("videos") val videos: TmdbResult<TmdbVideo>? = null,
    @SerialName("images") val images: TmdbImages? = null
) : TmdbRatingItem {

    val posterImage get(): TmdbImage? = TmdbImage.poster(posterPath)
    val backdropImage get(): TmdbImage? = TmdbImage.backdrop(backdropPath)
}

fun TmdbResult<TmdbReleaseDates>.getCertification(country: String): String? =
    getReleaseDatesBy(country)
        ?.sortedBy { it.type }
        ?.find { !it.certification.isNullOrBlank() }
        ?.certification

fun TmdbResult<TmdbReleaseDates>.getReleaseDateBy(country: String): TmdbReleaseDate? =
    getReleaseDatesBy(country)?.minByOrNull { it.type }

fun TmdbResult<TmdbReleaseDates>.getReleaseDatesBy(country: String): List<TmdbReleaseDate>? =
    results.find { it.iso3166 == country }?.releaseDates

@Serializable
data class TmdbReleaseDates(
    @SerialName("iso_3166_1") val iso3166: String,
    @SerialName("release_dates") val releaseDates: List<TmdbReleaseDate>
)

@Serializable
data class TmdbReleaseDate(
    @SerialName("iso_639_1") val iso639: String? = null,
    @SerialName("release_date") val releaseDate: Instant?,
    @SerialName("certification") val certification: String? = null,
    @SerialName("type") val type: TmdbReleaseType
)

@Serializable
data class TmdbCountry(
    @SerialName("iso_3166_1") val iso3166: String,
    val name: String
)

@Serializable
data class TmdbBelongsToCollection(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("parts") val parts: List<TmdbMovie>
)

@Serializable
data class TmdbReview(
    @SerialName("id") val id: String,
    val author: String,
    val content: String,
    val url: String
)

@Serializable
data class TmdbAlternativeTitle(
    @SerialName("iso_3166_1") val iso3166: String,
    @SerialName("title") val title: String,
    @SerialName("type") val type: String? = null
)
