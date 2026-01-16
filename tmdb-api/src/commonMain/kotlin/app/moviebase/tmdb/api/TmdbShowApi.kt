package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterAppendResponses
import app.moviebase.tmdb.core.parameterIncludeImageLanguage
import app.moviebase.tmdb.core.parameterLanguage
import app.moviebase.tmdb.core.parameterPage
import app.moviebase.tmdb.model.AppendResponse
import app.moviebase.tmdb.model.TmdbAccountStates
import app.moviebase.tmdb.model.TmdbAggregateCredits
import app.moviebase.tmdb.model.TmdbAlternativeTitle
import app.moviebase.tmdb.model.TmdbAlternativeTitles
import app.moviebase.tmdb.model.TmdbContentRating
import app.moviebase.tmdb.model.TmdbCredits
import app.moviebase.tmdb.model.TmdbEpisodeGroup
import app.moviebase.tmdb.model.TmdbExternalIds
import app.moviebase.tmdb.model.TmdbImages
import app.moviebase.tmdb.model.TmdbKeyword
import app.moviebase.tmdb.model.TmdbList
import app.moviebase.tmdb.model.TmdbPageResult
import app.moviebase.tmdb.model.TmdbResult
import app.moviebase.tmdb.model.TmdbReview
import app.moviebase.tmdb.model.TmdbScreenedTheatrically
import app.moviebase.tmdb.model.TmdbShowDetail
import app.moviebase.tmdb.model.TmdbShowPageResult
import app.moviebase.tmdb.model.TmdbStatusResponse
import app.moviebase.tmdb.model.TmdbTranslations
import app.moviebase.tmdb.model.TmdbVideo
import app.moviebase.tmdb.model.TmdbWatchProviderResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class TmdbShowApi internal constructor(private val client: HttpClient) {

    suspend fun getDetails(
        showId: Int,
        language: String? = null,
        appendResponses: Iterable<AppendResponse>? = null,
        includeImageLanguages: String? = null,
    ): TmdbShowDetail = client.get {
        endPointShow(showId)
        parameterLanguage(language)
        parameterAppendResponses(appendResponses)
        parameterIncludeImageLanguage(includeImageLanguages)
    }.body()

    suspend fun getTranslations(showId: Int): TmdbTranslations = client.get {
        endPointShow(showId, "translations")
    }.body()

    /**
     * Get the primary TV show details by id.
     * @see [Documentation] (https://developers.themoviedb.org/3/tv/get-tv-details)
     *
     * @param includeImageLanguage If you want to include a fallback language, should be a comma seperated value like 'en,null'
     */
    suspend fun getImages(
        showId: Int,
        language: String? = null,
        includeImageLanguage: String? = null,
    ): TmdbImages = client.get {
        endPointShow(showId, "images")
        parameterLanguage(language)
        parameterIncludeImageLanguage(includeImageLanguage)
    }.body()

    suspend fun getAggregateCredits(
        showId: Int,
        language: String? = null,
    ): TmdbAggregateCredits = client.get {
        endPointShow(showId, "aggregate_credits")
        parameterLanguage(language)
    }.body()

    suspend fun getRecommendations(
        showId: Int,
        page: Int,
        language: String? = null,
    ): TmdbShowPageResult = client.get {
        endPointShow(showId, "recommendations")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getWatchProviders(showId: Int): TmdbWatchProviderResult = client.get {
        endPointShow(showId, "watch", "providers")
    }.body()

    suspend fun popular(
        page: Int,
        language: String? = null,
    ): TmdbShowPageResult = client.get {
        endPointV3("tv", "popular")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getAiringToday(
        page: Int = 1,
        language: String? = null,
        timezone: String? = null,
    ): TmdbShowPageResult = client.get {
        endPointV3("tv", "airing_today")
        parameterPage(page)
        parameterLanguage(language)
        timezone?.let { parameter("timezone", it) }
    }.body()

    suspend fun getOnTheAir(
        page: Int = 1,
        language: String? = null,
        timezone: String? = null,
    ): TmdbShowPageResult = client.get {
        endPointV3("tv", "on_the_air")
        parameterPage(page)
        parameterLanguage(language)
        timezone?.let { parameter("timezone", it) }
    }.body()

    suspend fun getTopRated(
        page: Int = 1,
        language: String? = null,
    ): TmdbShowPageResult = client.get {
        endPointV3("tv", "top_rated")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getSimilar(
        showId: Int,
        page: Int = 1,
        language: String? = null,
    ): TmdbShowPageResult = client.get {
        endPointShow(showId, "similar")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getCredits(showId: Int, language: String? = null): TmdbCredits = client.get {
        endPointShow(showId, "credits")
        parameterLanguage(language)
    }.body()

    suspend fun getVideos(showId: Int, language: String? = null): TmdbResult<TmdbVideo> = client.get {
        endPointShow(showId, "videos")
        parameterLanguage(language)
    }.body()

    suspend fun getReviews(
        showId: Int,
        page: Int = 1,
        language: String? = null,
    ): TmdbPageResult<TmdbReview> = client.get {
        endPointShow(showId, "reviews")
        parameterPage(page)
        parameterLanguage(language)
    }.body()

    suspend fun getKeywords(showId: Int): TmdbResult<TmdbKeyword> = client.get {
        endPointShow(showId, "keywords")
    }.body()

    suspend fun getAlternativeTitles(showId: Int): TmdbResult<TmdbAlternativeTitles> = client.get {
        endPointShow(showId, "alternative_titles")
    }.body()

    suspend fun getContentRatings(showId: Int): TmdbResult<TmdbContentRating> = client.get {
        endPointShow(showId, "content_ratings")
    }.body()

    suspend fun getExternalIds(showId: Int): TmdbExternalIds = client.get {
        endPointShow(showId, "external_ids")
    }.body()

    suspend fun getAccountStates(
        showId: Int,
        sessionId: String? = null,
        guestSessionId: String? = null,
    ): TmdbAccountStates = client.get {
        endPointShow(showId, "account_states")
        sessionId?.let { parameter("session_id", it) }
        guestSessionId?.let { parameter("guest_session_id", it) }
    }.body()

    suspend fun getEpisodeGroups(showId: Int, language: String? = null): TmdbResult<TmdbEpisodeGroup> = client.get {
        endPointShow(showId, "episode_groups")
        parameterLanguage(language)
    }.body()

    suspend fun getLists(
        showId: Int,
        language: String? = null,
        page: Int = 1,
    ): TmdbPageResult<TmdbList> = client.get {
        endPointShow(showId, "lists")
        parameterLanguage(language)
        parameterPage(page)
    }.body()

    suspend fun getScreenedTheatrically(showId: Int): TmdbResult<TmdbScreenedTheatrically> = client.get {
        endPointShow(showId, "screened_theatrically")
    }.body()

    suspend fun rateShow(
        showId: Int,
        rating: Float,
        sessionId: String? = null,
        guestSessionId: String? = null,
    ): TmdbStatusResponse = client.post {
        endPointShow(showId, "rating")
        contentType(ContentType.Application.Json)
        setBody(RatingRequest(rating))
        sessionId?.let { parameter("session_id", it) }
        guestSessionId?.let { parameter("guest_session_id", it) }
    }.body()

    suspend fun deleteRating(
        showId: Int,
        sessionId: String? = null,
        guestSessionId: String? = null,
    ): TmdbStatusResponse = client.delete {
        endPointShow(showId, "rating")
        sessionId?.let { parameter("session_id", it) }
        guestSessionId?.let { parameter("guest_session_id", it) }
    }.body()

    suspend fun getLatest(language: String? = null): TmdbShowDetail = client.get {
        endPointV3("tv", "latest")
        parameterLanguage(language)
    }.body()

    @Serializable
    private data class RatingRequest(
        val value: Float,
    )

    private fun HttpRequestBuilder.endPointShow(showId: Int, vararg paths: String) {
        endPointV3("tv", showId.toString(), *paths)
    }
}
