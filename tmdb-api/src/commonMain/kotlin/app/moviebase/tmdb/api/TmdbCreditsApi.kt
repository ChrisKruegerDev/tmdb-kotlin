package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.model.TmdbCredits
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TmdbCreditsApi internal constructor(private val client: HttpClient) {

    suspend fun getDetails(creditId: String): TmdbCredits = client.get {
        endPointV3("credit", creditId)
    }.body()
}
