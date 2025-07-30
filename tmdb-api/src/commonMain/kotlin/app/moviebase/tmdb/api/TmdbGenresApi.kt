package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterLanguage
import app.moviebase.tmdb.model.TmdbGenresResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TmdbGenresApi internal constructor(private val client: HttpClient) {

    suspend fun getMovieList(language: String? = null): TmdbGenresResponse = client.get {
        endPointV3("genre", "movie", "list")
        parameterLanguage(language)
    }.body()

    suspend fun getTvList(language: String? = null): TmdbGenresResponse = client.get {
        endPointV3("genre", "tv", "list")
        parameterLanguage(language)
    }.body()
}
