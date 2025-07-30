package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.endPointV3
import app.moviebase.tmdb.core.parameterPage
import app.moviebase.tmdb.model.TmdbChangedMoviesResponse
import app.moviebase.tmdb.model.TmdbChangedPeopleResponse
import app.moviebase.tmdb.model.TmdbChangedTvResponse
import app.moviebase.tmdb.model.TmdbChangesResponse
import kotlinx.datetime.LocalDate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TmdbChangesApi internal constructor(private val client: HttpClient) {

    suspend fun getMovieChanges(
        movieId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 1
    ): TmdbChangesResponse = client.get {
        endPointV3("movie", movieId.toString(), "changes")
        startDate?.let { parameter("start_date", it.toString()) }
        endDate?.let { parameter("end_date", it.toString()) }
        parameterPage(page)
    }.body()

    suspend fun getTvChanges(
        tvId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 1
    ): TmdbChangesResponse = client.get {
        endPointV3("tv", tvId.toString(), "changes")
        startDate?.let { parameter("start_date", it.toString()) }
        endDate?.let { parameter("end_date", it.toString()) }
        parameterPage(page)
    }.body()

    suspend fun getPersonChanges(
        personId: Int,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 1
    ): TmdbChangesResponse = client.get {
        endPointV3("person", personId.toString(), "changes")
        startDate?.let { parameter("start_date", it.toString()) }
        endDate?.let { parameter("end_date", it.toString()) }
        parameterPage(page)
    }.body()

    suspend fun getMovieChangeList(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 1
    ): TmdbChangedMoviesResponse = client.get {
        endPointV3("movie", "changes")
        startDate?.let { parameter("start_date", it.toString()) }
        endDate?.let { parameter("end_date", it.toString()) }
        parameterPage(page)
    }.body()

    suspend fun getTvChangeList(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 1
    ): TmdbChangedTvResponse = client.get {
        endPointV3("tv", "changes")
        startDate?.let { parameter("start_date", it.toString()) }
        endDate?.let { parameter("end_date", it.toString()) }
        parameterPage(page)
    }.body()

    suspend fun getPersonChangeList(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 1
    ): TmdbChangedPeopleResponse = client.get {
        endPointV3("person", "changes")
        startDate?.let { parameter("start_date", it.toString()) }
        endDate?.let { parameter("end_date", it.toString()) }
        parameterPage(page)
    }.body()
}
