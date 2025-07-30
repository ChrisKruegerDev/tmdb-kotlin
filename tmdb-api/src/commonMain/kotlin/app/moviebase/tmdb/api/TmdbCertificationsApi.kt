package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.model.TmdbMovieCertifications
import app.moviebase.tmdb.model.TmdbTvCertifications
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TmdbCertificationsApi internal constructor(private val client: HttpClient) {

    suspend fun getMovieCertifications(): TmdbMovieCertifications = client.get {
        endPointV3("certification", "movie", "list")
    }.body()

    suspend fun getTvCertifications(): TmdbTvCertifications = client.get {
        endPointV3("certification", "tv", "list")
    }.body()
}
