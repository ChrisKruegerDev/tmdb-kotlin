package app.moviebase.tmdb.api

import app.moviebase.tmdb.model.AppendResponse
import app.moviebase.tmdb.model.TmdbVideoType
import app.moviebase.tmdb.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TmdbShowsApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "tv/96677?language=en-US&append_to_response=external_ids,videos,credits,aggregate_credits,reviews,content_ratings,watch/providers"
                to "tv/tv_details_96677.json",
            "tv/96677?language=de-DE&append_to_response=images" to "tv/tv_details_96677_with_images.json",
            "tv/96677/images?language=en&include_image_language=en,null" to "tv/tv_images_96677.json",
            "tv/96677/aggregate_credits?language=en-US" to "tv/tv_aggregate_credits_96677.json",
            "tv/96677/recommendations?page=1&language=en-US" to "tv/tv_recommendations_96677.json",
            "tv/popular?page=1&language=en-US" to "tv/tv_popular.json",
        )
    )

    val classToTest = TmdbShowApi(client)

    @Nested
    inner class `when fetching show details` {

        @Test
        fun `it can fetch show details`() = runTest {
            val showDetails = classToTest.getDetails(
                showId = 96677,
                language = "en-US",
                appendResponses = listOf(
                    AppendResponse.EXTERNAL_IDS,
                    AppendResponse.VIDEOS,
                    AppendResponse.CREDITS,
                    AppendResponse.AGGREGATE_CREDITS,
                    AppendResponse.REVIEWS,
                    AppendResponse.CONTENT_RATING,
                    AppendResponse.WATCH_PROVIDERS
                )
            )

            assertThat(showDetails.id).isEqualTo(96677)
            assertThat(showDetails.videos).isNotNull()
            assertThat(showDetails.popularity).isEqualTo(183.04f)
            assertThat(showDetails.voteAverage).isEqualTo(7.9f)
            assertThat(showDetails.voteCount).isEqualTo(742)
            assertThat(showDetails.homepage).isEqualTo("https://www.netflix.com/title/80994082")
            assertThat(showDetails.overview).isEqualTo("Inspired by the adventures of Arsène Lupin, gentleman thief Assane Diop sets out to avenge his father for an injustice inflicted by a wealthy family.")

            val tmdbVideo = showDetails.videos?.results?.first()
            assertThat(tmdbVideo).isNotNull()
            assertThat(tmdbVideo?.id).isEqualTo("5f6dbe399f37b000365d22ad")
            assertThat(tmdbVideo?.type).isEqualTo(TmdbVideoType.TEASER)

            val network = showDetails.networks.first()
            assertThat(network).isNotNull()
            assertThat(network.name).isEqualTo("Netflix")
            assertThat(network.logoPath).isEqualTo("/wwemzKWzjKYJFfCeiB57q3r4Bcm.png")
        }

        @Test
        fun `with images only`() = runTest {
            val showDetails = classToTest.getDetails(
                showId = 96677,
                language = "de-DE",
                appendResponses = listOf(
                    AppendResponse.IMAGES
                )
            )

            val images = showDetails.images
            assertThat(images).isNotNull()
            assertThat(images?.posters).isNotEmpty()
            assertThat(images?.backdrops).isNotEmpty()
            assertThat(images?.logos).isNotEmpty()
        }
    }

    @Test
    fun `it can fetch aggregate credits`() = runTest {
        val aggregateCredits = classToTest.getAggregateCredits(
            showId = 96677,
            language = "en-US"
        )

        assertThat(aggregateCredits.cast).isNotEmpty()
        assertThat(aggregateCredits.crew).isNotEmpty()
    }

    @Test
    fun `it can fetch recommendations`() = runTest {
        val recommendations = classToTest.getRecommendations(
            showId = 96677,
            page = 1,
            language = "en-US"
        )

        assertThat(recommendations.results).isNotEmpty()
        assertThat(recommendations.page).isEqualTo(1)
    }

    @Test
    fun `it should return images from show`() = runTest {
        val images = classToTest.getImages(
            showId = 96677,
            language = "en",
            includeImageLanguage = "en,null"
        )

        assertThat(images.id).isEqualTo(96677)
        assertThat(images.backdrops.size).isEqualTo(14)
        assertThat(images.posters.size).isEqualTo(3)
        assertThat(images.logos.size).isEqualTo(3)

        val poster = images.posters.first()
        assertThat(poster.height).isEqualTo(1500)
        assertThat(poster.width).isEqualTo(1000)
        assertThat(poster.aspectRatio).isEqualTo(0.667f)
        assertThat(poster.iso639).isEqualTo("en")
        assertThat(poster.filePath).isEqualTo("/sOUWRai0215iUSMackrZx3Y1j05.jpg")
        assertThat(poster.voteAverage).isEqualTo(5.312f)
        assertThat(poster.voteCount).isEqualTo(1)
    }

    @Test
    fun `it can fetch popular shows`() = runTest {
        val popular = classToTest.popular(
            page = 1,
            language = "en-US"
        )

        assertThat(popular.results).isNotEmpty()
        assertThat(popular.page).isEqualTo(1)
    }
}
