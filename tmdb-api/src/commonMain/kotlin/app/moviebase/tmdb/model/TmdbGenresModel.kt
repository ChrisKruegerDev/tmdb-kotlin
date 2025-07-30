package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbGenresResponse(
    @SerialName("genres") val genres: List<TmdbGenre>
)

object TmdbGenreId {

    object Movie {
        const val ACTION = 28
        const val ADVENTURE = 12
        const val ANIMATION = 16
        const val COMEDY = 35
        const val CRIME = 80
        const val DOCUMENTARY = 99
        const val DRAMA = 18
        const val FAMILY = 10751
        const val FANTASY = 14
        const val HISTORY = 36
        const val HORROR = 27
        const val MUSIC = 10402
        const val MYSTERY = 9648
        const val ROMANCE = 10749
        const val SCIENCE_FICTION = 878
        const val TV_MOVIE = 10770
        const val THRILLER = 53
        const val WAR = 10752
        const val WESTERN = 37

        val ALL = setOf(
            ACTION,
            ADVENTURE,
            ANIMATION,
            COMEDY,
            CRIME,
            DOCUMENTARY,
            DRAMA,
            FAMILY,
            FANTASY,
            HISTORY,
            HORROR,
            MUSIC,
            MYSTERY,
            ROMANCE,
            SCIENCE_FICTION,
            TV_MOVIE,
            THRILLER,
            WAR,
            WESTERN,
        )

        fun contains(genreId: Int) = ALL.contains(genreId)
    }

    object Show {
        const val ACTION_ADVENTURE = 10759
        const val ANIMATION = 16
        const val COMEDY = 35
        const val CRIME = 80
        const val DOCUMENTARY = 99
        const val DRAMA = 18
        const val FAMILY = 10751
        const val KIDS = 10762
        const val MYSTERY = 9648
        const val NEWS = 10763
        const val REALITY = 10764
        const val SCIENCE_FICTION_FANTASY = 10765
        const val SOAP = 10766
        const val TALK = 10767
        const val WAR_POLITICS = 10768
        const val WESTERN = 37

        val ALL = setOf(
            ACTION_ADVENTURE,
            ANIMATION,
            COMEDY,
            CRIME,
            DOCUMENTARY,
            DRAMA,
            FAMILY,
            KIDS,
            MYSTERY,
            NEWS,
            REALITY,
            SCIENCE_FICTION_FANTASY,
            SOAP,
            TALK,
            WAR_POLITICS,
            WESTERN,
        )

        fun contains(genreId: Int) = ALL.contains(genreId)
    }
}
