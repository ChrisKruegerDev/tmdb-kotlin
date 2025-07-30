package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterLanguage
import app.moviebase.tmdb.model.TmdbList
import app.moviebase.tmdb.model.TmdbListCreateResponse
import app.moviebase.tmdb.model.TmdbListDetail
import app.moviebase.tmdb.model.TmdbListItemStatus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

/**
 * Fetches the Lists form TMDB version 3.
 * Note: When to use version 4, see [Documentation] (https://developers.themoviedb.org/3/lists/v4-or-v3-lists)
 */
class TmdbListsApi internal constructor(private val client: HttpClient) {

    suspend fun getDetails(
        listId: String,
        language: String? = null
    ): TmdbListDetail = client.get {
        endPointV3("list", listId)
        parameterLanguage(language)
    }.body()

    suspend fun checkItemStatus(
        listId: String,
        movieId: Int
    ): TmdbListItemStatus = client.get {
        endPointV3("list", listId, "item_status")
        parameter("movie_id", movieId)
    }.body()

    suspend fun createList(
        name: String,
        description: String,
        language: String = "en"
    ): TmdbListCreateResponse = client.post {
        endPointV3("list")
        contentType(ContentType.Application.Json)
        setBody(CreateListRequest(name, description, language))
    }.body()

    suspend fun addMovie(
        listId: String,
        mediaId: Int
    ): TmdbListCreateResponse = client.post {
        endPointV3("list", listId, "add_item")
        contentType(ContentType.Application.Json)
        setBody(AddMediaRequest(mediaId))
    }.body()

    suspend fun removeMovie(
        listId: String,
        mediaId: Int
    ): TmdbListCreateResponse = client.post {
        endPointV3("list", listId, "remove_item")
        contentType(ContentType.Application.Json)
        setBody(AddMediaRequest(mediaId))
    }.body()

    suspend fun clearList(listId: String): TmdbListCreateResponse = client.post {
        endPointV3("list", listId, "clear")
        contentType(ContentType.Application.Json)
        setBody(ConfirmRequest(true))
    }.body()

    suspend fun deleteList(listId: String): TmdbListCreateResponse = client.delete {
        endPointV3("list", listId)
    }.body()

    @Serializable
    private data class CreateListRequest(
        val name: String,
        val description: String,
        val language: String
    )

    @Serializable
    private data class AddMediaRequest(
        val media_id: Int
    )

    @Serializable
    private data class ConfirmRequest(
        val confirm: Boolean
    )
}
