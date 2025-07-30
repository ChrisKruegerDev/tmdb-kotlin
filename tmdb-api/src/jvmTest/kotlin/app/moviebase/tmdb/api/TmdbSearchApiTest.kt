
package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TmdbSearchApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "search/tv?query=Lupin&include_adult=false&page=1&region=US&language=en"
              to "search/search_tv_lupin.json",
            "search/tv?query=Simpsons&include_adult=false&page=1&region=US&language=en&first_air_date_year=1996"
              to "search/search_tv_simpsons.json",
            "search/tv?query=S.W.A.T.&include_adult=false&page=1&region=US&language=en"
              to "search/search_tv_SWAT.json",
            "search/movie?query=Star+Wars&include_adult=false&page=1&region=US&language=en"
              to "search/search_movie_star_wars.json",
            "search/movie?query=Star+Wars&include_adult=false&page=1&region=US&language=en&year=1977"
              to "search/search_movie_star_wars_1977.json",
            "search/movie?query=Star+Wars&include_adult=false&page=1&region=US&language=en&primary_release_year=1977"
              to "search/search_movie_star_wars_1977.json",
            "search/person?query=ka&include_adult=false&page=1&region=US&language=en"
              to "search/search_person_ka.json",
            "search/company?query=fox&page=1"
              to "search/search_company_fox.json",
            "search/collection?query=fast&page=1&language=en"
              to "search/search_collection_fast.json",
            "search/keyword?query=future&page=1"
              to "search/search_keyword_future.json",
            "search/multi?query=Brad&include_adult=false&page=1&region=US&language=en"
              to "search/search_multi_brad.json",
            "search/movie?query=ralph&include_adult=false&page=4&region=US&language=en"
              to "search/search_movie_ralph_with_collection.json"
        )
    )

    val classToTest = TmdbSearchApi(client)

    @Test
    fun `it can search shows by query Simpsons on 1996`() = runTest {
        val pageResult = classToTest.findShows("Simpsons", 1, "en", "US", false, firstAirDateYear = 1996)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(1)
        assertThat(pageResult.totalResults).isEqualTo(1)
        assertThat(pageResult.results).isNotEmpty()
        val show = pageResult.results.first()
        assertThat(show.id).isEqualTo(22983)
    }

    @Test
    fun `it can do a multi search with person and movie results by query brad`() = runTest {
        val pageResult = classToTest.findMulti("Brad", 1, "en", "US", false)

        assertEquals(1, pageResult.page)
        assertEquals(289, pageResult.totalPages)
        assertEquals(5771, pageResult.totalResults)
        assertTrue(pageResult.results.isNotEmpty())

        val show = pageResult.results.first()
        assertThat(show.id).isEqualTo(2126)

        val person = pageResult.results[1]
    }

    @Test
    fun `it can search shows by query Lupin`() = runTest {
        val pageResult = classToTest.findShows("Lupin", 1, "en", "US", false)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(1)
        assertThat(pageResult.totalResults).isEqualTo(15)
        assertThat(pageResult.results).isNotEmpty()
        val show = pageResult.results.first()
        assertThat(show.id).isEqualTo(96677)
    }

    @Test
    fun `it can search shows by query SWAT`() = runTest {
        val pageResult = classToTest.findShows("S.W.A.T.", 1, "en", "US", false)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(1)
        assertThat(pageResult.totalResults).isEqualTo(4)
        assertThat(pageResult.results).isNotEmpty()
        val show = pageResult.results.first()
        assertThat(show.id).isEqualTo(71790)
    }

    @Test
    fun `it can search movies by query Star Wars with primary release year`() = runTest {
        val pageResult = classToTest.findMovies("Star Wars", 1, "en", "US", false, primaryReleaseYear = 1977)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(1)
        assertThat(pageResult.totalResults).isEqualTo(2)
        assertThat(pageResult.results).isNotEmpty()
        val movie = pageResult.results.first()
        assertThat(movie.id).isEqualTo(11)
        assertThat(movie.title).isEqualTo("Star Wars")
    }

    @Test
    fun `it can search movies by query Star Wars on year 1977`() = runTest {
        val pageResult = classToTest.findMovies("Star Wars", 1, "en", "US", false, year = 1977)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(1)
        assertThat(pageResult.totalResults).isEqualTo(2)
        assertThat(pageResult.results).isNotEmpty()
        val movie = pageResult.results.first()
        assertThat(movie.id).isEqualTo(11)
        assertThat(movie.title).isEqualTo("Star Wars")
    }

    @Test
    fun `it can search movies by query Star Wars`() = runTest {
        val pageResult = classToTest.findMovies("Star Wars", 1, "en", "US", false)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(7)
        assertThat(pageResult.totalResults).isEqualTo(138)
        assertThat(pageResult.results).isNotEmpty()
        val movie = pageResult.results.first()
        assertThat(movie.id).isEqualTo(11)
        assertThat(movie.title).isEqualTo("Star Wars")
    }

    @Test
    fun `it can search persons by query ka`() = runTest {
        val pageResult = classToTest.findPeople("ka", 1, "en", "US", false)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(500)
        assertThat(pageResult.totalResults).isEqualTo(10_000)
        assertThat(pageResult.results).isNotEmpty()
        val person = pageResult.results.first()
        assertThat(person.id).isEqualTo(1194168)
        assertThat(person.name).isEqualTo("Shing Ka")
    }

    @Test
    fun `it can search companies by query fox`() = runTest {
        val pageResult = classToTest.findCompanies("fox", 1)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(8)
        assertThat(pageResult.totalResults).isEqualTo(153)
        assertThat(pageResult.results).isNotEmpty()
        val company = pageResult.results.first()
        assertThat(company.id).isEqualTo(5924)
        assertThat(company.name).isEqualTo("Fox")
    }

    @Test
    fun `it can search collections by query fast`() = runTest {
        val pageResult = classToTest.findCollections("fast", 1, "en")

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(1)
        assertThat(pageResult.totalResults).isEqualTo(5)
        assertThat(pageResult.results).isNotEmpty()
        val collection = pageResult.results.first()
        assertThat(collection.id).isEqualTo(257378)
        assertThat(collection.name).isEqualTo("Fast Getaway Collection")
        assertThat(collection.backdropPath).isNull()
    }

    @Test
    fun `it can search keyword by query future`() = runTest {
        val pageResult = classToTest.findKeywords("future", 1)

        assertThat(pageResult.page).isEqualTo(1)
        assertThat(pageResult.totalPages).isEqualTo(1)
        assertThat(pageResult.totalResults).isEqualTo(4)
        assertThat(pageResult.results).isNotEmpty()
        val company = pageResult.results.first()
        assertThat(company.id).isEqualTo(797559)
        assertThat(company.name).isEqualTo("Future Collection")
    }

    @Test
    fun `it can handle movie collections in search results without crashing`() = runTest {
        val pageResult = classToTest.findMovies("ralph", 4, "en", "US", false)

        assertThat(pageResult.page).isEqualTo(4)
        assertThat(pageResult.totalPages).isEqualTo(4)
        assertThat(pageResult.totalResults).isEqualTo(30)
        assertThat(pageResult.results).hasSize(2)
        
        val regularMovie = pageResult.results[0]
        assertThat(regularMovie.id).isEqualTo(123456)
        assertThat(regularMovie.title).isEqualTo("Test Movie")
        assertThat(regularMovie.genresIds).containsExactly(28, 878)
        assertThat(regularMovie.popularity).isEqualTo(75.5f)
        assertThat(regularMovie.voteCount).isEqualTo(100)
        assertThat(regularMovie.video).isFalse()
        assertThat(regularMovie.voteAverage).isEqualTo(7.5f)
        
        val collection = pageResult.results[1]
        assertThat(collection.id).isEqualTo(1468084)
        assertThat(collection.title).isEqualTo("The Ralph Mouse Collection")
        assertThat(collection.genresIds).isEmpty()
        assertThat(collection.popularity).isEqualTo(0f)
        assertThat(collection.voteCount).isEqualTo(0)
        assertThat(collection.video).isFalse()
        assertThat(collection.voteAverage).isEqualTo(0f)
    }
}
