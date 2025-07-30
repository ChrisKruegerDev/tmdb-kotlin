package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.model.TmdbReviewDetail
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TmdbReviewsApi internal constructor(private val client: HttpClient) {

    suspend fun getDetails(reviewId: String): TmdbReviewDetail = client.get {
        endPointV3("review", reviewId)
    }.body()
}
