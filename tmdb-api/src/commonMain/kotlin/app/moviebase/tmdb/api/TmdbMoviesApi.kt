package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterAppendResponses
import app.moviebase.tmdb.core.parameterIncludeImageLanguage
import app.moviebase.tmdb.core.parameterIncludeVideoLanguage
import app.moviebase.tmdb.core.parameterLanguage
import app.moviebase.tmdb.core.parameterPage
import app.moviebase.tmdb.model.AppendResponse
import app.moviebase.tmdb.model.TmdbAccountStates
import app.moviebase.tmdb.model.TmdbAlternativeTitles
import app.moviebase.tmdb.model.TmdbCredits
import app.moviebase.tmdb.model.TmdbExternalIds
import app.moviebase.tmdb.model.TmdbImages
import app.moviebase.tmdb.model.TmdbKeyword
import app.moviebase.tmdb.model.TmdbMovieDetail
import app.moviebase.tmdb.model.TmdbMoviePageResult
import app.moviebase.tmdb.model.TmdbPageResult
import app.moviebase.tmdb.model.TmdbReleaseDates
import app.moviebase.tmdb.model.TmdbResult
import app.moviebase.tmdb.model.TmdbReview
import app.moviebase.tmdb.model.TmdbStatusResponse
import app.moviebase.tmdb.model.TmdbTranslations
import app.moviebase.tmdb.model.TmdbVideo
import app.moviebase.tmdb.model.TmdbWatchProviderResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class TmdbMoviesApi internal constructor(private val client: HttpClient) {

    /**
     * Get the primary information about a movie.
     */
    suspend fun getDetails(
        movieId: Int,
        language: String? = null,
        appendResponses: Iterable<AppendResponse>? = null,
        includeImageLanguages: String? = null,
        includeVideoLanguages: String? = null,
    ): TmdbMovieDetail = client.get(moviePath(movieId).joinToString(separator = "/")) {
        parameterLanguage(language)
        parameterAppendResponses(appendResponses)
        parameterIncludeImageLanguage(includeImageLanguages)
        parameterIncludeVideoLanguage(includeVideoLanguages)
    }.body()

    suspend fun getImages(
        movieId: Int,
        language: String? = null,
        includeImageLanguage: String? = null,
    ): TmdbImages = client.get(moviePath(movieId, "images").joinToString(separator = "/")) {
        parameterLanguage(language)
        parameterIncludeImageLanguage(includeImageLanguage)
    }.body()

    suspend fun getExternalIds(movieId: Int): TmdbExternalIds =
        client.get(moviePath(movieId, "external_ids").joinToString(separator = "/")).body()

    suspend fun getTranslations(movieId: Int): TmdbTranslations =
        client.get(moviePath(movieId, "translations").joinToString(separator = "/")).body()

    suspend fun getWatchProviders(movieId: Int): TmdbWatchProviderResult =
        client.get(moviePath(movieId, "watch", "providers").joinToString(separator = "/")).body()

    suspend fun popular(
        page: Int,
        language: String? = null,
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", "popular")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getNowPlaying(
        page: Int = 1,
        language: String? = null,
        region: String? = null,
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", "now_playing")
        parameterPage(page)
        parameterLanguage(language)
        region?.let { parameter("region", it) }
    }.body()

    suspend fun getTopRated(
        page: Int = 1,
        language: String? = null,
        region: String? = null,
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", "top_rated")
        parameterPage(page)
        parameterLanguage(language)
        region?.let { parameter("region", it) }
    }.body()

    suspend fun getUpcoming(
        page: Int = 1,
        language: String? = null,
        region: String? = null,
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", "upcoming")
        parameterPage(page)
        parameterLanguage(language)
        region?.let { parameter("region", it) }
    }.body()

    suspend fun getSimilar(
        movieId: Int,
        page: Int = 1,
        language: String? = null,
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", movieId.toString(), "similar")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getRecommendations(
        movieId: Int,
        page: Int = 1,
        language: String? = null,
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", movieId.toString(), "recommendations")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getCredits(movieId: Int, language: String? = null): TmdbCredits =
        client.get(moviePath(movieId, "credits").joinToString(separator = "/")) {
            parameterLanguage(language)
        }.body()

    suspend fun getVideos(movieId: Int, language: String? = null): TmdbResult<TmdbVideo> =
        client.get(moviePath(movieId, "videos").joinToString(separator = "/")) {
            parameterLanguage(language)
        }.body()

    suspend fun getReviews(
        movieId: Int,
        page: Int = 1,
        language: String? = null,
    ): TmdbPageResult<TmdbReview> = client.get {
        endPointV3("movie", movieId.toString(), "reviews")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getKeywords(movieId: Int): TmdbResult<TmdbKeyword> =
        client.get(moviePath(movieId, "keywords").joinToString(separator = "/")).body()

    suspend fun getAlternativeTitles(
        movieId: Int,
        country: String? = null,
    ): TmdbResult<TmdbAlternativeTitles> =
        client.get(moviePath(movieId, "alternative_titles").joinToString(separator = "/")) {
            country?.let { parameter("country", it) }
        }.body()

    suspend fun getReleaseDates(movieId: Int): TmdbResult<TmdbReleaseDates> =
        client.get(moviePath(movieId, "release_dates").joinToString(separator = "/")).body()

    suspend fun getAccountStates(
        movieId: Int,
        sessionId: String? = null,
        guestSessionId: String? = null,
    ): TmdbAccountStates = client.get {
        endPointV3("movie", movieId.toString(), "account_states")
        sessionId?.let { parameter("session_id", it) }
        guestSessionId?.let { parameter("guest_session_id", it) }
    }.body()

    suspend fun rateMovie(
        movieId: Int,
        rating: Float,
        sessionId: String? = null,
        guestSessionId: String? = null,
    ): TmdbStatusResponse = client.post {
        endPointV3("movie", movieId.toString(), "rating")
        contentType(ContentType.Application.Json)
        setBody(RatingRequest(rating))
        sessionId?.let { parameter("session_id", it) }
        guestSessionId?.let { parameter("guest_session_id", it) }
    }.body()

    suspend fun deleteRating(
        movieId: Int,
        sessionId: String? = null,
        guestSessionId: String? = null,
    ): TmdbStatusResponse = client.delete {
        endPointV3("movie", movieId.toString(), "rating")
        sessionId?.let { parameter("session_id", it) }
        guestSessionId?.let { parameter("guest_session_id", it) }
    }.body()

    suspend fun getLatest(language: String? = null): TmdbMovieDetail = client.get {
        endPointV3("movie", "latest")
        parameterLanguage(language)
    }.body()

    @Serializable
    private data class RatingRequest(
        val value: Float,
    )

    private fun moviePath(movieId: Int, vararg paths: String) = arrayOf("movie", movieId.toString(), *paths)
}
