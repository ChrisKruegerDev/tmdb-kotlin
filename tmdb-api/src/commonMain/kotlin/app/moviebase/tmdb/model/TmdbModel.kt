@file:Suppress("ktlint:trailing-comma-on-declaration-site", "ktlint:no-semi")

package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TmdbExternalSource(val value: String) {
    @SerialName("imdb_id")
    IMDB("imdb_id"),

    @SerialName("tvdb_id")
    TVDB("tvdb_id");
}

enum class AppendResponse(val value: String) {
    VIDEOS("videos"),
    RELEASES_DATES("release_dates"),
    REVIEWS("reviews"),
    CREDITS("credits"),
    AGGREGATE_CREDITS("aggregate_credits"),
    COMBINED_CREDITS("combined_credits"),
    TRANSLATIONS("translations"),
    IMAGES("images"),
    TAGGED_IMAGES("tagged_images"),
    EXTERNAL_IDS("external_ids"),
    CONTENT_RATING("content_ratings"),
    MOVIE_CREDITS("movie_credits"),
    TV_CREDITS("tv_credits"),
    WATCH_PROVIDERS("watch/providers");

    companion object {
        fun build(appendResponses: Iterable<AppendResponse>) = appendResponses.joinToString(",") { it.value }
    }
}

enum class TmdbSortOrder(val value: String) {
    ASC("asc"), DESC("desc")
}

fun TmdbSortOrder?.getValueOrDefault() = this?.value ?: TmdbSortOrder.DESC?.value

/**
 * TMDB returns some errors like no resources, invalid API key, no token has been granted.
 * @see [Documentation] (https://developers.themoviedb.org/4/account/get-account-rated-movies)
 */
object TmdbStatusCode {
    const val SUCCESS_ADDED = 1
    const val SUCCESS_UPDATED = 12
    const val SUCCESS_DELETED = 13

    const val AUTHENTICATION_FAILED = 3
    const val INVALID_API_KEY = 7
    const val RESOURCE_NOT_FOUND = 34
    const val TOKEN_NOT_GRANTED = 36
}

@Serializable
data class TmdbResult<T>(
    val results: List<T>
)

interface TmdbPageResult<T> {
    val page: Int
    val results: List<T>
    val totalResults: Int
    val totalPages: Int
}

@Serializable
data class TmdbErrorResponse(
    @SerialName("success") val success: Boolean = false,
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String,
    @SerialName("error_message") val errorMessage: String? = null
)

@Serializable
data class TmdbStatusResult(
    @SerialName("status_message") val statusMessage: String? = null,
    @SerialName("error_message") val errorMessage: String? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("success") val success: Boolean? = null,
    @SerialName("status_code") val statusCode: Int
)

@Serializable
data class TmdbTranslations(
    val id: Int,
    val translations: List<TmdbTranslation>
)

@Serializable
data class TmdbTranslation(
    @SerialName("iso_3166_1") val iso3166: String,
    @SerialName("iso_639_1") val iso639: String,
    val name: String,
    @SerialName("english_name") val englishName: String
)

@Serializable
data class TmdbGenre(
    val id: Int,
    val name: String
)

@Serializable
data class TmdbKeyword(
    val id: Int,
    val name: String
)

@Serializable
data class TmdbExternalIds(
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("freebase_mid") val freebaseMid: String? = null,
    @SerialName("freebase_id") val freebaseId: String? = null,
    @SerialName("tvdb_id") val tvdbId: Int? = null,
    @SerialName("tvrage_id") val tvrageId: Int? = null,
    @SerialName("id") val id: Int? = null, // it is is used in append responses
    @SerialName("facebook_id") val facebook: String? = null,
    @SerialName("instagram_id") val instagram: String? = null,
    @SerialName("tiktok_id") val tiktok: String? = null,
    @SerialName("twitter_id") val twitter: String? = null,
    @SerialName("wikidata_id") val wikidata: String? = null,
    @SerialName("youtube_id") val youtube: String? = null,
)

@Serializable
enum class TmdbVideoType(val value: String) {
    @SerialName("Trailer")
    TRAILER("Trailer"),

    @SerialName("Teaser")
    TEASER("Teaser"),

    @SerialName("Clip")
    CLIP("Clip"),

    @SerialName("Featurette")
    FEATURETTE("Featurette"),

    @SerialName("Bloopers")
    BLOOPERS("Bloopers"),

    @SerialName("Opening Credits")
    OPENING_CREDITS("Opening Credits"),

    @SerialName("Behind the Scenes")
    BEHIND_THE_SCENES("Behind the Scenes");
}

@Serializable
enum class TmdbVideoSite(val value: String) {
    @SerialName("YouTube")
    YOUTUBE("YouTube"),

    @SerialName("Vimeo")
    VIMEO("Vimeo");
}

/**
 *
 */
@Serializable
data class TmdbVideo(
    @SerialName("id") val id: String,
    @SerialName("iso_639_1") val iso639: String? = null,
    @SerialName("iso_3166_1") val iso3166: String? = null,
    @SerialName("key") val key: String? = null,
    @SerialName("site") val site: TmdbVideoSite? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("size") val size: Int? = null, // 360, 480, 720, 1080
    @SerialName("type") val type: TmdbVideoType? = null
)

@Serializable
data class TmdbImages(
    @SerialName("id") val id: Int? = null,
    @SerialName("posters") val posters: List<TmdbFileImage> = emptyList(),
    @SerialName("backdrops") val backdrops: List<TmdbFileImage> = emptyList(),
    @SerialName("logos") val logos: List<TmdbFileImage> = emptyList(),
    @SerialName("stills") val stills: List<TmdbFileImage> = emptyList()
)

@Serializable
data class TmdbFileImage(
    @SerialName("file_path") val filePath: String,
    @SerialName("aspect_ratio") val aspectRation: Float,
    @SerialName("height") val height: Int,
    @SerialName("width") val width: Int,
    @SerialName("iso_639_1") val iso639: String? = null,
    @SerialName("vote_average") val voteAverage: Float? = null,
    @SerialName("vote_count") val voteCount: Int? = null
)

@Serializable
data class TmdbLogoImage(
    @SerialName("file_path") val filePath: String?
)

enum class TmdbRequestMediaType(val value: String) {
    ALL("all"),
    MOVIE("movie"),
    TV("tv"),
    PERSON("person")
}

enum class TmdbTimeWindow(val value: String) {
    DAY("day"), WEEK("week")
}
