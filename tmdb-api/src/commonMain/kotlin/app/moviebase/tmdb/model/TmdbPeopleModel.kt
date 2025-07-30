package app.moviebase.tmdb.model

import app.moviebase.tmdb.core.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object TmdbCrewJobType {

    const val DIRECTOR = "Director"
    const val PRODUCER = "Producer"
    const val WRITER = "Writer"
    const val STORY = "Story"
    const val SCREENPLAY = "Screenplay"
    const val CHARACTERS = "Characters"
    const val ART_DIRECTION = "Art Direction"
    const val EDITOR = "Editor"
    const val NOVEL = "Novel"
    const val EXECUTIVE_PRODUCER = "Executive Producer"

    val importantJobs = listOf(DIRECTOR, PRODUCER, WRITER, STORY, SCREENPLAY, CHARACTERS)
}

@Serializable
enum class TmdbGender(val value: Int) {
    @SerialName("0")
    UNKNOWN(0),

    @SerialName("1")
    FEMALE(1),

    @SerialName("2")
    MALE(2),

    @SerialName("3")
    NON_BINARY(3)
}

@Serializable
data class TmdbCredits(
    @SerialName("cast") val cast: List<TmdbCast>,
    @SerialName("crew") val crew: List<TmdbCrew>,
) {

    /**
     * Groups the crew by job.
     */
    fun getGroupedCrew(): Map<String, List<TmdbCrew>> {
        val jobsSet = TmdbCrewJobType.importantJobs.toSet()
        return crew.filter { jobsSet.contains(it.job) }.groupBy { it.job }
    }

    /**
     * Sorts the crew by most important job.
     */
    fun getSortedCrew(): List<TmdbCrew> {
        val jobsSet = TmdbCrewJobType.importantJobs.toSet()
        val orderByJob =
            TmdbCrewJobType.importantJobs.withIndex().associate { it.value to it.index }
        return crew.filter { jobsSet.contains(it.job) }.sortedBy { orderByJob[it.job] }
    }
}

interface TmdbAnyPerson : TmdbAnyItem, TmdbProfileItem {
    val name: String
    val popularity: Float?
}

@Serializable
data class TmdbAggregateCredits(
    @SerialName("cast") val cast: List<TmdbAggregateCast>,
    @SerialName("crew") val crew: List<TmdbAggregateCrew>,
)

@Serializable
@SerialName("person")
data class TmdbPerson(
    @SerialName("adult") val adult: Boolean,
    @SerialName("gender") val gender: TmdbGender,
    @SerialName("id") override val id: Int,
    @SerialName("known_for_department") val knownForDepartment: TmdbDepartment? = null,
    @SerialName("name") override val name: String,
    @SerialName("profile_path") override val profilePath: String? = null,
    @SerialName("popularity") override val popularity: Float
) : TmdbAnyPerson, TmdbSearchableListItem

@Serializable
data class TmdbPersonPageResult(
    @SerialName("page") override val page: Int,
    @SerialName("results") override val results: List<TmdbPerson> = emptyList(),
    @SerialName("total_results") override val totalResults: Int,
    @SerialName("total_pages") override val totalPages: Int
) : TmdbPageResult<TmdbPerson>

@Serializable
data class TmdbPersonDetail(
    @SerialName("adult") val adult: Boolean,
    @SerialName("also_known_as") val alsoKnownAs: List<String>,
    @SerialName("biography") val biography: String? = null,
    @SerialName("birthday") val birthday: LocalDate? = null,
    @SerialName("deathday") val deathday: LocalDate? = null,
    @SerialName("gender") val gender: TmdbGender? = null,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("id") override val id: Int,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("known_for_department") val knownForDepartment: TmdbDepartment? = null,
    @SerialName("name") override val name: String,
    @SerialName("place_of_birth") val placeOfBirth: String? = null,
    @SerialName("popularity") override val popularity: Float? = null,
    @SerialName("profile_path") override val profilePath: String? = null,
    @SerialName("external_ids") val externalIds: TmdbExternalIds? = null,
    @SerialName("images") val images: TmdbPersonImages? = null,
    @SerialName("tagged_images") val taggedImages: TmdbImagePageResult? = null,
    @SerialName("combined_credits") val combinedCredits: TmdbPersonCombinedCredits? = null,
    @SerialName("movie_credits") val movieCredits: TmdbPersonMovieCredits? = null,
    @SerialName("tv_credits") val tvCredits: TmdbPersonShowCredits? = null,
    @SerialName("translations") val translations: TmdbPersonTranslations? = null,
) : TmdbAnyPerson

@Serializable
data class TmdbCrew(
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("gender") val gender: TmdbGender = TmdbGender.UNKNOWN,
    @SerialName("id") override val id: Int,
    @SerialName("known_for_department") val knownForDepartment: TmdbDepartment? = null,
    @SerialName("name") override val name: String,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("popularity") override val popularity: Float? = null,
    @SerialName("profile_path") override val profilePath: String? = null,
    @SerialName("credit_id") val creditId: String,
    @SerialName("department") val department: TmdbDepartment? = null,
    @SerialName("job") val job: String,
) : TmdbAnyPerson

@Serializable
data class TmdbCast(
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("gender") val gender: TmdbGender,
    @SerialName("id") override val id: Int,
    @SerialName("known_for_department") val knownForDepartment: TmdbDepartment? = null,
    @SerialName("name") override val name: String,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("popularity") override val popularity: Float? = null,
    @SerialName("profile_path") override val profilePath: String? = null,
    @SerialName("cast_id") val castId: Int? = null,
    @SerialName("character") val character: String,
    @SerialName("credit_id") val creditId: String,
    @SerialName("order") val order: Int,
) : TmdbAnyPerson

@Serializable
data class TmdbAggregateCast(
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("gender") val gender: TmdbGender,
    @SerialName("id") val id: Int,
    @SerialName("known_for_department") val knownForDepartment: TmdbDepartment? = null,
    @SerialName("name") val name: String,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("popularity") val popularity: Float? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("roles") val roles: List<TmdbRole>,
    @SerialName("total_episode_count") val totalEpisodeCount: Int,
    @SerialName("order") val order: Int,
)

@Serializable
data class TmdbAggregateCrew(
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("gender") val gender: TmdbGender,
    @SerialName("id") override val id: Int,
    @SerialName("known_for_department") val knownForDepartment: TmdbDepartment? = null,
    @SerialName("name") override val name: String,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("popularity") override val popularity: Float? = null,
    @SerialName("profile_path") override val profilePath: String? = null,
    @SerialName("jobs") val jobs: List<TmdbJob>,
    @SerialName("department") val department: TmdbDepartment? = null,
    @SerialName("total_episode_count") val totalEpisodeCount: Int,
) : TmdbAnyPerson

@Serializable
data class TmdbRole(
    @SerialName("credit_id") val creditId: String,
    @SerialName("character") val character: String,
    @SerialName("episode_count") val episodeCount: Int,
)

@Serializable
data class TmdbJob(
    @SerialName("credit_id") val creditId: String,
    @SerialName("job") val job: String,
    @SerialName("episode_count") val episodeCount: Int,
)

@Serializable
data class TmdbTaggedImage(
    @SerialName("media") val media: TmdbTaggedMedia,
)

@Serializable
data class TmdbPersonTranslations(
    @SerialName("translations") val translations: List<TmdbPersonTranslation>,
)

@Serializable
data class TmdbPersonTranslation(
    @SerialName("iso_3166_1") val iso3166: String,
    @SerialName("iso_639_1") val iso639: String,
    @SerialName("name") val name: String,
    @SerialName("english_name") val englishName: String,
    @SerialName("data") val data: TmdbPersonTranslationData
)

@Serializable
data class TmdbPersonTranslationData(
    @SerialName("biography")  val biography: String
)

@Serializable
data class TmdbShowCreatedBy(
    @SerialName("id") val id: Int,
    @SerialName("credit_id") val creditId: String? = null,
    @SerialName("gender") val gender: TmdbGender? = null,
    @SerialName("name") val name: String,
    @SerialName("profile_path") val profilePath: String? = null,
)

@Serializable
data class TmdbImagePageResult(
    @SerialName("page") override val page: Int,
    @SerialName("results") override val results: List<TmdbTaggedImage> = emptyList(),
    @SerialName("total_results") override val totalResults: Int,
    @SerialName("total_pages") override val totalPages: Int,
) : TmdbPageResult<TmdbTaggedImage>

@Serializable
data class TmdbTaggedMedia(
    @SerialName("backdrop_path") val backdropPath: String? = null,
)

@Serializable
data class TmdbPersonImages(
    @SerialName("profiles") val profiles: List<TmdbFileImage> = emptyList(),
)

typealias TmdbPersonShowCredits = TmdbPersonCredits<TmdbPersonCredit.Show>
typealias TmdbPersonMovieCredits = TmdbPersonCredits<TmdbPersonCredit.Movie>
typealias TmdbPersonCombinedCredits = TmdbPersonCredits<TmdbPersonCredit>
typealias TmdbPersonTaggedImages = TmdbImagePageResult

@Serializable
data class TmdbPersonCredits<T : TmdbPersonCredit>(
    @SerialName("cast") val cast: List<T>,
    @SerialName("crew") val crew: List<T>,
)

@Polymorphic
@Serializable
sealed interface TmdbPersonCredit : TmdbAnyItem, TmdbBackdropItem, TmdbPosterItem {

    val voteAverage: Float
    val voteCount: Int
    val overview: String
    val genreIds: List<Int>
    val popularity: Float?
    val originalLanguage: String?
    val adult: Boolean

    val character: String?
    val creditId: String?
    val order: Int?
    val department: TmdbDepartment?
    val job: String?

    @Serializable
    @SerialName("movie")
    data class Movie(
        @SerialName("poster_path") override val posterPath: String?,
        @SerialName("adult") override val adult: Boolean = false,
        @SerialName("overview") override val overview: String,
        @SerialName("release_date")
        @Serializable(LocalDateSerializer::class)
        val releaseDate: LocalDate? = null,
        @SerialName("genre_ids") override val genreIds: List<Int>,
        @SerialName("id") override val id: Int,
        @SerialName("original_title") val originalTitle: String? = null,
        @SerialName("original_language") override val originalLanguage: String,
        @SerialName("title") val title: String? = null,
        @SerialName("backdrop_path") override val backdropPath: String?,
        @SerialName("popularity") override val popularity: Float,
        @SerialName("video") val video: Boolean = false,
        @SerialName("vote_average") override val voteAverage: Float,
        @SerialName("vote_count") override val voteCount: Int,
        @SerialName("character") override val character: String? = null,
        @SerialName("credit_id") override val creditId: String? = null,
        @SerialName("order") override val order: Int? = null,
        @SerialName("department") override val department: TmdbDepartment? = null,
        @SerialName("job") override val job: String? = null,
    ) : TmdbPersonCredit

    @Serializable
    @SerialName("tv")
    data class Show(
        @SerialName("poster_path") override val posterPath: String?  = null,
        @SerialName("popularity") override val popularity: Float? = null,
        @SerialName("id") override val id: Int,
        @SerialName("adult") override val adult: Boolean = false,
        @SerialName("backdrop_path") override val backdropPath: String? = null,
        @SerialName("vote_average") override val voteAverage: Float,
        @SerialName("overview") override val overview: String,
        @SerialName("first_air_date")
        @Serializable(LocalDateSerializer::class)
        val firstAirDate: LocalDate? = null,
        @SerialName("origin_country") val originCountry: List<String> = emptyList(),
        @SerialName("genre_ids") override val genreIds: List<Int> = emptyList(),
        @SerialName("original_language") override val originalLanguage: String,
        @SerialName("vote_count") override val voteCount: Int,
        @SerialName("name") val name: String? = null,
        @SerialName("original_name") val originalName: String? = null,
        @SerialName("character") override val character: String? = null,
        @SerialName("credit_id") override val creditId: String? = null,
        @SerialName("order") override val order: Int? = null,
        @SerialName("department") override val department: TmdbDepartment? = null,
        @SerialName("job") override val job: String? = null,
    ) : TmdbPersonCredit
}

@Serializable
enum class TmdbDepartment(val value: String) {

    @SerialName("Acting")
    ACTING("Acting"),

    @SerialName("Writing")
    WRITING("Writing"),

    @SerialName("Sound")
    SOUND("Sound"),

    @SerialName("Production")
    PRODUCTION("Production"),

    @SerialName("Art")
    ART("Art"),

    @SerialName("Directing")
    DIRECTING("Directing"),

    @SerialName("Creator")
    CREATOR("Creator"),

    @SerialName("Costume & Make-Up")
    COSTUME_AND_MAKEUP("Costume & Make-Up"),

    @SerialName("Camera")
    CAMERA("Camera"),

    @SerialName("Visual Effects")
    VISUAL_EFFECTS("Visual Effects"),

    @SerialName("Lighting")
    LIGHTING("Lighting"),

    @SerialName("Editing")
    EDITING("Editing"),

    @SerialName("Actors")
    ACTORS("Actors"),

    @SerialName("Crew")
    CREW("Crew");

    companion object {
        fun of(value: String?) = entries.find { it.value == value }
    }
}
