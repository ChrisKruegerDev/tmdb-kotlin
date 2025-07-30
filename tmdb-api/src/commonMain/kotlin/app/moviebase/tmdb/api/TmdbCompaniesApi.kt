package app.moviebase.tmdb.api

import app.moviebase.tmdb.model.*
import app.moviebase.tmdb.core.endPointV3
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TmdbCompaniesApi internal constructor(private val client: HttpClient) {

    suspend fun getDetails(companyId: Int): TmdbCompanyDetail = client.get {
        endPointV3("company", companyId.toString())
    }.body()

    suspend fun getAlternativeNames(companyId: Int): TmdbResult<TmdbAlternativeName> = client.get {
        endPointV3("company", companyId.toString(), "alternative_names")
    }.body()

    suspend fun getImages(companyId: Int): TmdbCompanyImages = client.get {
        endPointV3("company", companyId.toString(), "images")
    }.body()
}
