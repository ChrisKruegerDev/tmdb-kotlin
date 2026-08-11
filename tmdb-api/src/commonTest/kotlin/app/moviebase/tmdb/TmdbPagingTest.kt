package app.moviebase.tmdb

import app.moviebase.tmdb.core.parameterPage
import app.moviebase.tmdb.model.TmdbPageResult
import app.moviebase.tmdb.model.accessibleTotalPages
import app.moviebase.tmdb.model.nextPage
import io.ktor.client.request.HttpRequestBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class TmdbPagingTest {

    @Test
    fun `reported pages are capped to the TMDB request limit`() {
        val result = TestPageResult(page = 1, totalPages = 1_000)

        assertEquals(500, result.accessibleTotalPages)
        assertEquals(2, result.nextPage)
    }

    @Test
    fun `page 500 has no successor when TMDB reports more pages`() {
        val result = TestPageResult(page = 500, totalPages = 1_000)

        assertNull(result.nextPage)
    }

    @Test
    fun `request builder rejects pages outside the TMDB range`() {
        HttpRequestBuilder().parameterPage(TmdbPaging.FIRST_PAGE)
        HttpRequestBuilder().parameterPage(TmdbPaging.MAX_PAGE)

        assertFailsWith<IllegalArgumentException> { HttpRequestBuilder().parameterPage(0) }
        assertFailsWith<IllegalArgumentException> { HttpRequestBuilder().parameterPage(501) }
    }

    private data class TestPageResult(
        override val page: Int,
        override val totalPages: Int,
        override val results: List<Unit> = emptyList(),
        override val totalResults: Int = 0,
    ) : TmdbPageResult<Unit>
}
