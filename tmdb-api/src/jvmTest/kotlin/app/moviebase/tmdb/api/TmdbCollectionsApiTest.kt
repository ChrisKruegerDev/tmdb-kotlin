package app.moviebase.tmdb.api

import app.moviebase.tmdb.core.mockHttpClient
import app.moviebase.tmdb.model.AppendResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TmdbCollectionsApiTest {

    val client = mockHttpClient(
        version = 3,
        responses = mapOf(
            "collection/420?language=en-US&append_to_response=images,translations" to "collections/collection_details_420.json",
        ),
    )

    val classToTest = TmdbCollectionsApi(client)

    @Test
    fun `it can get company details`() = runTest {
        val collectionDetail = classToTest.getDetails(
            collectionId = 420,
            language = "en-US",
            appendResponses = listOf(AppendResponse.IMAGES, AppendResponse.TRANSLATIONS),
        )

        assertThat(collectionDetail.id).isEqualTo(420)
        assertThat(collectionDetail.name).isEqualTo("The Chronicles of Narnia Collection")
        assertThat(collectionDetail.posterPath).isEqualTo("/sh6Kn8VBfXotJ6qsvJkdfscxXKR.jpg")

        val firstItem = collectionDetail.parts.first()
        assertThat(firstItem.id).isEqualTo(411)
        assertThat(firstItem.title).isEqualTo("The Chronicles of Narnia: The Lion, the Witch and the Wardrobe")
        assertThat(firstItem.releaseDate).isEqualTo(LocalDate(year = 2005, month = 12, day = 7))

        assertThat(collectionDetail.images).isNotNull()

        assertThat(collectionDetail.translations).isNotNull()
        val italianTranslation = collectionDetail.translations!!.translations.first { it.iso639 == "it" }
        assertThat(italianTranslation.data.title).isEqualTo("Le cronache di Narnia - Collezione")
    }
}
