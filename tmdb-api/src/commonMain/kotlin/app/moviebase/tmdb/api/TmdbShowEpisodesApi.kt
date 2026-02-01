package app.moviebase.tmdb.api

import app.moviebase.tmdb.model.AppendResponse
import app.moviebase.tmdb.model.TmdbEpisodeDetail
import app.moviebase.tmdb.model.TmdbExternalIds
import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterAppendResponses
import app.moviebase.tmdb.core.parameterIncludeImageLanguage
import app.moviebase.tmdb.core.parameterIncludeVideoLanguage
import app.moviebase.tmdb.core.parameterLanguage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TmdbShowEpisodesApi(private val client: HttpClient) {

    suspend fun getDetails(
        showId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        language: String? = null,
        appendResponses: Iterable<AppendResponse>? = null,
        includeImageLanguages: String? = null,
        includeVideoLanguages: String? = null,
    ): TmdbEpisodeDetail = client.get {
        endPointEpisode(showId, seasonNumber, episodeNumber)
        parameterLanguage(language)
        parameterAppendResponses(appendResponses)
        parameterIncludeImageLanguage(includeImageLanguages)
        parameterIncludeVideoLanguage(includeVideoLanguages)
    }.body()

    suspend fun getExternalIds(showId: Int, seasonNumber: Int, episodeNumber: Int): TmdbExternalIds = client.get {
        endPointEpisode(showId, seasonNumber, episodeNumber, "external_ids")
    }.body()

    private fun HttpRequestBuilder.endPointEpisode(showId: Int, seasonNumber: Int, episodeNumber: Int, vararg paths: String) {
        endPointV3("tv", showId.toString(), "season", seasonNumber.toString(), "episode", episodeNumber.toString(), *paths)
    }
}
