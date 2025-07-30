package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.mockHttpClient
import app.moviebase.tmdb.model.AppendResponse
import app.moviebase.tmdb.model.TmdbDepartment
import app.moviebase.tmdb.model.TmdbGender
import com.google.common.truth.Truth.assertThat
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TmdbPeopleApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "person/287?language=en-US&append_to_response=external_ids,images,tagged_images,movie_credits,tv_credits"
                to "person/person_details_287.json",
            "person/19292/movie_credits?language=en-US"
                to "person/person_movie_credits_19292.json",
            "person/19292/tv_credits?language=en-US"
                to "person/person_tv_credits_19292.json",
            "person/11701?language=en-DE&append_to_response=external_ids,images,tagged_images,combined_credits,movie_credits,tv_credits,translations"
                to "person/person_details_11701_all_responses.json",
        ),
    )

    val classToTest = TmdbPeopleApi(client)

    @Test
    fun `it can fetch person details`() = runTest {
        val personDetail = classToTest.getDetails(
            personId = 287,
            language = "en-US",
            appendResponses = listOf(
                AppendResponse.EXTERNAL_IDS,
                AppendResponse.IMAGES,
                AppendResponse.TAGGED_IMAGES,
                AppendResponse.MOVIE_CREDITS,
                AppendResponse.TV_CREDITS,
            ),
        )

        assertThat(personDetail.id).isEqualTo(287)
        assertThat(personDetail.externalIds).isNotNull()
        assertThat(personDetail.movieCredits).isNotNull()
        assertThat(personDetail.tvCredits).isNotNull()
    }

    /**
     * external_ids,images,tagged_images,combined_credits,movie_credits,tv_credits,translations
     */
    @Test
    fun `it can fetch person details with all responses`() = runTest {
        val personDetail = classToTest.getDetails(
            personId = 11701,
            language = "en-DE",
            appendResponses = listOf(
                AppendResponse.EXTERNAL_IDS,
                AppendResponse.IMAGES,
                AppendResponse.TAGGED_IMAGES,
                AppendResponse.COMBINED_CREDITS,
                AppendResponse.MOVIE_CREDITS,
                AppendResponse.TV_CREDITS,
                AppendResponse.TRANSLATIONS,
            ),
        )

        // Add your assertions here
        assertEquals(false, personDetail.adult)
        assertTrue(personDetail.biography!!.startsWith("Angelina Jolie is an American actress"))
        assertEquals(LocalDate.parse("1975-06-04"), personDetail.birthday)
        assertEquals(null, personDetail.deathday)
        assertEquals(TmdbGender.FEMALE, personDetail.gender)
        assertEquals("https://www.unhcr.org/pages/49c3646c56.html", personDetail.homepage)
        assertEquals(11701, personDetail.id)
        assertEquals("nm0001401", personDetail.imdbId)
        assertEquals(TmdbDepartment.ACTING, personDetail.knownForDepartment)
        assertEquals("Angelina Jolie", personDetail.name)
        assertEquals("Los Angeles, California, USA ", personDetail.placeOfBirth)
        assertEquals(41.859f, personDetail.popularity)
        assertEquals("/9kkfIiufGORw4bELN79DuTaKqAy.jpg", personDetail.profilePath)
        assertEquals("nm0001401", personDetail.externalIds?.imdbId)
        assertEquals("Q13909", personDetail.externalIds?.wikidata)
        assertEquals("", personDetail.externalIds?.facebook)
        assertEquals("angelinajolie", personDetail.externalIds?.instagram)
        assertEquals(null, personDetail.externalIds?.tvdbId)
        assertEquals("", personDetail.externalIds?.twitter)
        assertEquals(null, personDetail.externalIds?.youtube)
    }

    @Test
    fun `it can fetch show credits`() = runTest {
        val showCredits = classToTest.getShowCredits(
            personId = 19292,
            language = "en-US",
        )

        assertThat(showCredits.cast).isNotNull()
        assertThat(showCredits.crew).isNotNull()
    }

    @Test
    fun `it can fetch movie credits`() = runTest {
        val showCredits = classToTest.getMovieCredits(
            personId = 19292,
            language = "en-US",
        )

        assertThat(showCredits.cast).isNotNull()
        assertThat(showCredits.crew).isNotNull()
    }
}
