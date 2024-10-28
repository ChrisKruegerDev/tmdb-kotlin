@file:Suppress("ktlint")

package app.moviebase.tmdb.api

import app.moviebase.tmdb.model.AppendResponse
import app.moviebase.tmdb.model.TmdbVideoType
import app.moviebase.tmdb.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TmdbShowEpisodesApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "tv/96677/season/1/episode/1?language=en-US&append_to_response=external_ids,videos,credits,aggregate_credits,reviews,content_ratings,watch/providers"
                    to "tv/episode/tv_details_96677_s1e1.json",
            "tv/96677/season/1/episode/1?language=de-DE&append_to_response=images" to "tv/episode/tv_details_96677_s1e1_with_images.json"
        )
    )

    val classToTest = TmdbShowEpisodesApi(client)

    @Nested
    inner class `when fetching episode details` {

        @Test
        fun `it can fetch episode details`() = runTest {
            val episodeDetails = classToTest.getDetails(
                showId = 96677,
                language = "en-US",
                seasonNumber = 1,
                episodeNumber = 1,
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

            assertThat(episodeDetails.id).isEqualTo(2613188)
            assertThat(episodeDetails.seasonNumber).isEqualTo(1)
            assertThat(episodeDetails.episodeNumber).isEqualTo(1)
            assertThat(episodeDetails.name).isEqualTo("Chapter 1")
            assertThat(episodeDetails.airDate).isEqualTo(LocalDate.parse("2021-01-08"))
            assertThat(episodeDetails.voteAverage).isEqualTo(8.189f)
            assertThat(episodeDetails.voteCount).isEqualTo(90)
        }

        @Test
        fun `with images only`() = runTest {
            val episodeDetails = classToTest.getDetails(
                showId = 96677,
                language = "de-DE",
                seasonNumber = 1,
                episodeNumber = 1,
                appendResponses = listOf(
                    AppendResponse.IMAGES
                )
            )

            val images = episodeDetails.images
            assertThat(images).isNotNull()
            assertThat(images?.stills).isNotEmpty()
        }
    }
}
