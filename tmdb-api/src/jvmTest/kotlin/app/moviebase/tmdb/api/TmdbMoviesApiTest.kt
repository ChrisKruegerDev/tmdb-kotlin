package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.mockHttpClient
import app.moviebase.tmdb.model.AppendResponse
import app.moviebase.tmdb.model.TmdbReleaseType
import app.moviebase.tmdb.model.TmdbVideoType
import app.moviebase.tmdb.model.getCertification
import app.moviebase.tmdb.model.getReleaseDateBy
import app.moviebase.tmdb.model.getReleaseDatesBy
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.toInstant
import org.junit.jupiter.api.Test

class TmdbMoviesApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "movie/10140?language=en-US&append_to_response=images,external_ids,videos,release_dates,credits,reviews,content_ratings,watch/providers"
                to "movie/movie_details_10140.json",
            "movie/10140/images?language=en"
                to "movie/movie_images_10140.json",
            "movie/607?language=en-US&append_to_response=external_ids,videos,release_dates,credits,reviews,content_ratings,watch/providers"
                to "movie/movie_details_607.json",
            "movie/526896?language=en-US&append_to_response=release_dates"
                to "movie/movie_images_526896.json",
            "movie/37799?language=en-US"
                to "movie/movie_details_37799.json",
            "movie/popular?page=1&language=en-US"
                to "movie/movie_popular.json",
        )
    )

    val classToTest = TmdbMoviesApi(client)

    @Test
    fun `it should return images from movie`() = runTest {
        val images = classToTest.getImages(movieId = 10140, language = "en")

        assertThat(images.id).isEqualTo(10140)
        assertThat(images.backdrops.size).isEqualTo(2)
        assertThat(images.posters.size).isEqualTo(14)
        assertThat(images.logos.size).isEqualTo(2)
    }

    @Test
    fun `it can fetch movie details`() = runTest {
        val movieDetails = classToTest.getDetails(
            movieId = 10140,
            language = "en-US",
            appendResponses = listOf(
                AppendResponse.IMAGES,
                AppendResponse.EXTERNAL_IDS,
                AppendResponse.VIDEOS,
                AppendResponse.RELEASES_DATES,
                AppendResponse.CREDITS,
                AppendResponse.REVIEWS,
                AppendResponse.CONTENT_RATING,
                AppendResponse.WATCH_PROVIDERS
            )
        )

        assertThat(movieDetails.id).isEqualTo(10140)
        assertThat(movieDetails.images).isNotNull()
        assertThat(movieDetails.videos).isNotNull()
        assertThat(movieDetails.popularity).isEqualTo(48.581f)
        assertThat(movieDetails.voteAverage).isEqualTo(6.4f)
        assertThat(movieDetails.voteCount).isEqualTo(4534)
        assertThat(movieDetails.overview).isEqualTo("This time around Edmund and Lucy Pevensie, along with their pesky cousin Eustace Scrubb find themselves swallowed into a painting and on to a fantastic Narnian ship headed for the very edges of the world.")

        val tmdbVideo = movieDetails.videos?.results?.first()
        assertThat(tmdbVideo).isNotNull()
        assertThat(tmdbVideo?.id).isEqualTo("54b36eb9c3a3680939006425")
        assertThat(tmdbVideo?.type).isEqualTo(TmdbVideoType.TRAILER)
    }

    @Test
    fun `it can get right certifications`() = runTest {
        val movieDetails = classToTest.getDetails(
            movieId = 607,
            language = "en-US",
            appendResponses = listOf(
                AppendResponse.EXTERNAL_IDS,
                AppendResponse.VIDEOS,
                AppendResponse.RELEASES_DATES,
                AppendResponse.CREDITS,
                AppendResponse.REVIEWS,
                AppendResponse.CONTENT_RATING,
                AppendResponse.WATCH_PROVIDERS
            )
        )

        val certification = movieDetails.releaseDates?.getCertification("FR")
        val releaseDate = movieDetails.releaseDates?.getReleaseDateBy("FR")
        val releaseDates = movieDetails.releaseDates?.getReleaseDatesBy("FR")

        assertThat(certification).isEqualTo("U")
        assertThat(releaseDates).hasSize(5)
        assertThat(releaseDate?.type).isEqualTo(TmdbReleaseType.THEATRICAL)
    }
    @Test
    fun `it can serialize release dates correctly`() = runTest {
        val movieDetails = classToTest.getDetails(
            movieId = 526896,
            language = "en-US",
            appendResponses = listOf(
                AppendResponse.RELEASES_DATES,
            )
        )

        val releaseDates = movieDetails.releaseDates?.getReleaseDatesBy("US")
        assertThat(releaseDates).hasSize(6)

        val expectedReleaseDates = listOf(
            "2022-04-01T00:00:00.000Z",
            "2022-05-17T00:00:00.000Z",
            "2022-09-07T00:00:00.000Z",
            "2024-03-01T00:00:00.000Z",
            "2022-06-14T00:00:00.000Z",
            "2024-03-20T00:00:00.000Z"
        ).map { it.toInstant() }

        val releaseDateInstants = releaseDates?.map { it.releaseDate }
        assertThat(releaseDateInstants).isEqualTo(expectedReleaseDates)
    }

    @Test
    fun `it can fetch popular movies`() = runTest {
        val popular = classToTest.popular(
            page = 1,
            language = "en-US"
        )

        assertThat(popular.results).isNotEmpty()
        assertThat(popular.page).isEqualTo(1)
    }

    @Test
    fun `it has the origin country`() = runTest {
        val movieDetails = classToTest.getDetails(
            movieId = 37799,
            language = "en-US",
        )

        assertThat(movieDetails.originCountry.size).isEqualTo(1)
        assertThat(movieDetails.originCountry.first()).isEqualTo("US")
    }
}
