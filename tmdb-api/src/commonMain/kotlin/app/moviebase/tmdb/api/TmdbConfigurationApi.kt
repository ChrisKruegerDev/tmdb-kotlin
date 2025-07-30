package app.moviebase.tmdb.api

import app.moviebase.tmdb.model.*
import app.moviebase.tmdb.core.endPointV3
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TmdbConfigurationApi internal constructor(private val client: HttpClient) {

    /**
     * Get the system wide configuration information.
     * @see [Documentation] (https://developers.themoviedb.org/3/configuration/get-api-configuration)
     */
    suspend fun getApiConfiguration(): TmdbConfiguration = client.get {
        endPointV3("configuration")
    }.body()

    /**
     * Get the list of countries (ISO 3166-1 tags) used throughout TMDB.
     * [Documentation](https://developer.themoviedb.org/reference/configuration-countries)
     */
    suspend fun getCountries(): List<TmdbConfigurationCountry> = client.get {
        endPointV3("configuration", "countries")
    }.body()

    suspend fun getJobs(): List<TmdbConfigurationJob> = client.get {
        endPointV3("configuration", "jobs")
    }.body()

    suspend fun getLanguages(): List<TmdbLanguage> = client.get {
        endPointV3("configuration", "languages")
    }.body()

    suspend fun getPrimaryTranslations(): List<String> = client.get {
        endPointV3("configuration", "primary_translations")
    }.body()

    suspend fun getTimezones(): List<TmdbTimezone> = client.get {
        endPointV3("configuration", "timezones")
    }.body()
}
