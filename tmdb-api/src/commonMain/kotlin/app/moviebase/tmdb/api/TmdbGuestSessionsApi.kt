package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterPage
import app.moviebase.tmdb.model.*
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TmdbGuestSessionsApi internal constructor(private val client: HttpClient) {

    suspend fun getRatedMovies(
        guestSessionId: String,
        language: String? = null,
        sortBy: String? = null,
        page: Int = 1
    ): TmdbMoviePageResult = client.get {
        endPointV3("guest_session", guestSessionId, "rated", "movies")
        language?.let { parameter("language", it) }
        sortBy?.let { parameter("sort_by", it) }
        parameterPage(page)
    }.body()

    suspend fun getRatedTv(
        guestSessionId: String,
        language: String? = null,
        sortBy: String? = null,
        page: Int = 1
    ): TmdbShowPageResult = client.get {
        endPointV3("guest_session", guestSessionId, "rated", "tv")
        language?.let { parameter("language", it) }
        sortBy?.let { parameter("sort_by", it) }
        parameterPage(page)
    }.body()

    suspend fun getRatedTvEpisodes(
        guestSessionId: String,
        language: String? = null,
        sortBy: String? = null,
        page: Int = 1
    ): TmdbPageResult<TmdbEpisodeDetail> = client.get {
        endPointV3("guest_session", guestSessionId, "rated", "tv", "episodes")
        language?.let { parameter("language", it) }
        sortBy?.let { parameter("sort_by", it) }
        parameterPage(page)
    }.body()
}
