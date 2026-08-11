package app.moviebase.tmdb

/** Paging limits enforced by the TMDB API. */
object TmdbPaging {
    const val FIRST_PAGE = 1
    const val MAX_PAGE = 500

    fun accessibleTotalPages(totalPages: Int): Int = totalPages.coerceIn(0, MAX_PAGE)
}
