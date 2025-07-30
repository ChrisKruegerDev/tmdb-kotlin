package app.moviebase.tmdb.api

import app.moviebase.tmdb.model.*
import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterLanguage
import app.moviebase.tmdb.core.parameterIncludeImageLanguage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TmdbCollectionsApi internal constructor(private val client: HttpClient) {

    suspend fun getDetails(collectionId: Int, language: String? = null): TmdbBelongsToCollection = client.get {
        endPointV3("collection", collectionId.toString())
        parameterLanguage(language)
    }.body()

    suspend fun getImages(
        collectionId: Int,
        language: String? = null,
        includeImageLanguage: String? = null
    ): TmdbImages = client.get {
        endPointV3("collection", collectionId.toString(), "images")
        parameterLanguage(language)
        parameterIncludeImageLanguage(includeImageLanguage)
    }.body()

    suspend fun getTranslations(collectionId: Int): TmdbTranslations = client.get {
        endPointV3("collection", collectionId.toString(), "translations")
    }.body()
}
