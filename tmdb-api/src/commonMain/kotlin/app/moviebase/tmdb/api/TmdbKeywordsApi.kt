package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.model.TmdbKeywordDetail
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get

class TmdbKeywordsApi internal constructor(private val client: HttpClient) {

    suspend fun getDetails(keywordId: Int): TmdbKeywordDetail = client.get {
        endPointV3("keyword", keywordId.toString())
    }.body()
}
