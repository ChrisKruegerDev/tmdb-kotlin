package app.moviebase.tmdb.model

import app.moviebase.tmdb.core.JsonFactory
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TmdbPeopleModelTest {

    private val json = JsonFactory.buildJson()

    @Test
    fun `it decodes a person with an empty known_for_department`() {
        val payload = """
            {
              "page": 1,
              "results": [
                { "id": 1, "name": "Jane Doe", "known_for_department": "Acting" },
                { "id": 2, "name": "John Doe", "known_for_department": "" }
              ],
              "total_results": 2,
              "total_pages": 1
            }
        """.trimIndent()

        val result = json.decodeFromString(TmdbPersonPageResult.serializer(), payload)

        assertThat(result.results[0].knownForDepartment).isEqualTo(TmdbDepartment.ACTING)
        assertThat(result.results[1].knownForDepartment).isNull()
    }
}
