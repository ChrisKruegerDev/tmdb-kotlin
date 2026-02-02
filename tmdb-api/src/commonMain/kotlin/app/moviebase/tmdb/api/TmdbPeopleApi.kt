package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterAppendResponses
import app.moviebase.tmdb.core.parameterLanguage
import app.moviebase.tmdb.core.parameterPage
import app.moviebase.tmdb.model.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TmdbPeopleApi internal constructor(private val client: HttpClient) {

    suspend fun getDetails(
        personId: Int,
        language: String? = null,
        appendResponses: Iterable<AppendResponse>? = null
    ): TmdbPersonDetail = client.get {
        endPointPerson(personId)
        parameterLanguage(language)
        parameterAppendResponses(appendResponses)
    }.body()

    suspend fun getShowCredits(
        personId: Int,
        language: String? = null
    ): TmdbPersonShowCredits = client.get {
        endPointPerson(personId, "tv_credits")
        parameterLanguage(language)
    }.body()

    suspend fun getMovieCredits(
        personId: Int,
        language: String? = null
    ): TmdbPersonMovieCredits = client.get {
        endPointPerson(personId, "movie_credits")
        parameterLanguage(language)
    }.body()

    suspend fun getCombinedCredits(
        personId: Int,
        language: String? = null
    ): TmdbPersonMovieCredits = client.get {
        endPointPerson(personId, "combined_credits")
        parameterLanguage(language)
    }.body()

    suspend fun getExternalIds(personId: Int): TmdbExternalIds = client.get {
        endPointPerson(personId, "external_ids")
    }.body()

    suspend fun getImages(personId: Int): TmdbImages = client.get {
        endPointPerson(personId, "images")
    }.body()

    suspend fun getTaggedImages(
        personId: Int,
        language: String? = null,
        page: Int = 1
    ): TmdbPersonTaggedImages = client.get {
        endPointPerson(personId, "tagged_images")
        parameterLanguage(language)
        parameterPage(page)
    }.body()

    suspend fun getTranslations(personId: Int): TmdbPersonTranslations = client.get {
        endPointPerson(personId, "translations")
    }.body()

    suspend fun getLatest(language: String? = null): TmdbPersonDetail = client.get {
        endPointV3("person", "latest")
        parameterLanguage(language)
    }.body()

    suspend fun getPopular(
        language: String? = null,
        page: Int = 1
    ): TmdbPersonPageResult = client.get {
        endPointV3("person", "popular")
        parameterLanguage(language)
        parameterPage(page)
    }.body()

    private fun HttpRequestBuilder.endPointPerson(personId: Int, vararg paths: String) {
        endPointV3("person", personId.toString(), *paths)
    }
}
