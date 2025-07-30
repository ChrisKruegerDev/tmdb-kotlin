package app.moviebase.tmdb.api

import app.moviebase.tmdb.model.*
import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.getByPaths
import app.moviebase.tmdb.core.parameterAppendResponses
import app.moviebase.tmdb.core.parameterIncludeImageLanguage
import app.moviebase.tmdb.core.parameterLanguage
import app.moviebase.tmdb.core.parameterPage
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
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
        appendResponses: Iterable<AppendResponse>? = null
    ): TmdbMovieDetail = client.getByPaths(*moviePath(movieId)) {
        parameterLanguage(language)
        parameterAppendResponses(appendResponses)
    }

    suspend fun getImages(
        movieId: Int,
        language: String? = null,
        includeImageLanguage: String? = null
    ): TmdbImages = client.getByPaths(*moviePath(movieId, "images")) {
        parameterLanguage(language)
        parameterIncludeImageLanguage(includeImageLanguage)
    }

    suspend fun getExternalIds(movieId: Int): TmdbExternalIds = client.getByPaths(*moviePath(movieId, "external_ids"))

    suspend fun getTranslations(movieId: Int): TmdbTranslations = client.getByPaths(*moviePath(movieId, "translations"))

    suspend fun getWatchProviders(movieId: Int): TmdbWatchProviderResult = client.getByPaths(*moviePath(movieId, "watch", "providers"))

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
        region: String? = null
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", "now_playing")
        parameterPage(page)
        parameterLanguage(language)
        region?.let { parameter("region", it) }
    }.body()

    suspend fun getTopRated(
        page: Int = 1,
        language: String? = null,
        region: String? = null
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", "top_rated")
        parameterPage(page)
        parameterLanguage(language)
        region?.let { parameter("region", it) }
    }.body()

    suspend fun getUpcoming(
        page: Int = 1,
        language: String? = null,
        region: String? = null
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", "upcoming")
        parameterPage(page)
        parameterLanguage(language)
        region?.let { parameter("region", it) }
    }.body()

    suspend fun getSimilar(
        movieId: Int,
        page: Int = 1,
        language: String? = null
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", movieId.toString(), "similar")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getRecommendations(
        movieId: Int,
        page: Int = 1,
        language: String? = null
    ): TmdbMoviePageResult = client.get {
        endPointV3("movie", movieId.toString(), "recommendations")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getCredits(movieId: Int, language: String? = null): TmdbCredits = 
        client.getByPaths(*moviePath(movieId, "credits")) {
            parameterLanguage(language)
        }

    suspend fun getVideos(movieId: Int, language: String? = null): TmdbResult<TmdbVideo> = 
        client.getByPaths(*moviePath(movieId, "videos")) {
            parameterLanguage(language)
        }

    suspend fun getReviews(
        movieId: Int,
        page: Int = 1,
        language: String? = null
    ): TmdbPageResult<TmdbReview> = client.get {
        endPointV3("movie", movieId.toString(), "reviews")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getKeywords(movieId: Int): TmdbResult<TmdbKeyword> = 
        client.getByPaths(*moviePath(movieId, "keywords"))

    suspend fun getAlternativeTitles(
        movieId: Int,
        country: String? = null
    ): TmdbResult<TmdbAlternativeTitle> = client.getByPaths(*moviePath(movieId, "alternative_titles")) {
        country?.let { parameter("country", it) }
    }

    suspend fun getReleaseDates(movieId: Int): TmdbResult<TmdbReleaseDates> = 
        client.getByPaths(*moviePath(movieId, "release_dates"))

    suspend fun getAccountStates(
        movieId: Int,
        sessionId: String? = null,
        guestSessionId: String? = null
    ): TmdbAccountStates = client.get {
        endPointV3("movie", movieId.toString(), "account_states")
        sessionId?.let { parameter("session_id", it) }
        guestSessionId?.let { parameter("guest_session_id", it) }
    }.body()

    suspend fun rateMovie(
        movieId: Int,
        rating: Float,
        sessionId: String? = null,
        guestSessionId: String? = null
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
        guestSessionId: String? = null
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
        val value: Float
    )

    private fun moviePath(movieId: Int, vararg paths: String) = arrayOf("movie", movieId.toString(), *paths)
}
