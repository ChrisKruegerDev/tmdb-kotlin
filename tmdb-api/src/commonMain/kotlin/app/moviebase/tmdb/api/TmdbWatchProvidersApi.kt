package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterLanguage
import app.moviebase.tmdb.core.parameterWatchRegion
import app.moviebase.tmdb.model.TmdbWatchProviderListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TmdbWatchProvidersApi internal constructor(private val client: HttpClient) {

    suspend fun getMovieList(watchRegion: String? = null, language: String? = null): TmdbWatchProviderListResponse =
        client.get {
            endPointV3("watch", "providers", "movie")
            parameterWatchRegion(watchRegion)
            parameterLanguage(language)
        }.body()

    suspend fun getTvList(watchRegion: String? = null, language: String? = null): TmdbWatchProviderListResponse =
        client.get {
            endPointV3("watch", "providers", "tv")
            parameterWatchRegion(watchRegion)
            parameterLanguage(language)
        }.body()
}
