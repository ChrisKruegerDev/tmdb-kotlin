package app.moviebase.tmdb.model

import kotlinx.datetime.LocalDate

enum class TmdbDiscoverMovieSortBy(val value: String) {
    POPULARITY("popularity"),
    RELEASE_DATE("release_date"),
    REVENUE("revenue"),
    PRIMARY_RELEASE_DATE("primary_release_date"),
    ORIGINAL_TITLE("original_title"),
    VOTE_AVERAGE("vote_average"),
    VOTE_COUNT("vote_count")
}

enum class TmdbDiscoverShowSortBy(val value: String) {
    POPULARITY("popularity"),
    VOTE_AVERAGE("vote_average"),
    FIRST_AIR_DATE("first_air_date"),
    NAME("name"),
    VOTE_COUNT("vote_count")
}

enum class TmdbDiscoverSeparator(val value: String) {
    AND(","), OR("|")
}

data class TmdbDiscoverFilter<T>(
    val separator: TmdbDiscoverSeparator = TmdbDiscoverSeparator.AND,
    val items: Collection<T>,
) {
    fun build(transform: (T) -> String): String {
        return items.joinToString(separator = separator.value, transform = transform)
    }
}

sealed interface TmdbDiscoverTimeRange {

    data class BetweenYears(
        private val from: Int,
        private val to: Int,
    ) : TmdbDiscoverTimeRange {
        val firstDayOfYear: String get() = LocalDate(from, 1, 1).toString()
        val lastDayOfYear: String get() = LocalDate(to, 12, 31).toString()
    }

    data class OneYear(
        val year: Int,
    ) : TmdbDiscoverTimeRange

    data class Custom(
        val firstDate: String? = null,
        val lastDate: String? = null,
    ) : TmdbDiscoverTimeRange
}

sealed interface TmdbDiscover {

    val sortOrder: TmdbSortOrder
    val voteAverageGte: Float?
    val voteAverageLte: Float?
    val voteCountGte: Int?
    val voteCountLte: Int?

    val withGenres: TmdbDiscoverFilter<String>?
    val withoutGenres: TmdbDiscoverFilter<String>?
    val withWatchProviders: TmdbDiscoverFilter<Int>?
    val watchRegion: String? // ISO 3166-1 code
    val withWatchMonetizationTypes: List<TmdbWatchMonetizationType>

    fun buildParameters(): Map<String, String?>

    fun newParameterMap(): HashMap<String, String?> {
        val params = HashMap<String, String?>()
        voteAverageGte?.let {
            params[DiscoverParam.VOTE_AVERAGE_GTE] = it.toString()
        }
        voteAverageLte?.let {
            params[DiscoverParam.VOTE_AVERAGE_LTE] = it.toString()
        }

        voteCountGte?.let {
            params[DiscoverParam.VOTE_COUNT_GTE] = it.toString()
        }

        voteCountLte?.let {
            params[DiscoverParam.VOTE_COUNT_LTE] = it.toString()
        }

        withGenres?.let {
            params[DiscoverParam.WITH_GENRES] = it.items.joinToString(it.separator.value)
        }

        withoutGenres?.let {
            params[DiscoverParam.WITHOUT_GENRES] = it.items.joinToString(it.separator.value)
        }

        withWatchProviders?.let { f ->
            params[DiscoverParam.WITH_WATCH_PROVIDERS] = f.build { it.toString() }
        }

        watchRegion?.let {
            params[DiscoverParam.WATCH_REGION] = it
        }

        if (withWatchMonetizationTypes.isNotEmpty()) {
            params[DiscoverParam.WITH_WATCH_MONETIZATION_TYPES] =
                withWatchMonetizationTypes.joinToString(",") { it.value }
        }

        return params
    }

    data class Movie(
        val includeAdult: Boolean = false,
        val includeVideo: Boolean = false,
        val language: String? = null,
        val region: String? = null,
        val sortBy: TmdbDiscoverMovieSortBy = TmdbDiscoverMovieSortBy.POPULARITY,
        override val sortOrder: TmdbSortOrder = TmdbSortOrder.DESC,
        override val voteAverageGte: Float? = null,
        override val voteAverageLte: Float? = null,
        override val voteCountGte: Int? = null,
        override val voteCountLte: Int? = null,
        override val withGenres: TmdbDiscoverFilter<String>? = null,
        override val withoutGenres: TmdbDiscoverFilter<String>? = null,
        val releaseDate: TmdbDiscoverTimeRange? = null,
        val primaryReleaseDate: TmdbDiscoverTimeRange? = null,
        val releaseYear: Int? = null,
        val primaryReleaseYear: Int? = null,
        val withReleaseTypes: TmdbDiscoverFilter<TmdbReleaseType>? = null,
        val certification: String? = null,
        val certificationGte: String? = null,
        val certificationLte: String? = null,
        val certificationCountry: String? = null,
        val withCast: TmdbDiscoverFilter<String>? = null,
        val withCrew: TmdbDiscoverFilter<String>? = null,
        val withPeople: TmdbDiscoverFilter<String>? = null,
        val withKeywords: TmdbDiscoverFilter<String>? = null,
        val withoutKeywords: TmdbDiscoverFilter<String>? = null,
        val withCompanies: TmdbDiscoverFilter<Int>? = null,
        val withoutCompanies: TmdbDiscoverFilter<Int>? = null,
        val withOriginCountries: TmdbDiscoverFilter<String>? = null,
        val withOriginalLanguage: String? = null,
        val withRuntimeGte: Int? = null,
        val withRuntimeLte: Int? = null,
        override val withWatchProviders: TmdbDiscoverFilter<Int>? = null,
        val withoutWatchProviders: TmdbDiscoverFilter<Int>? = null,
        override val watchRegion: String? = null,
        override val withWatchMonetizationTypes: List<TmdbWatchMonetizationType> = emptyList(),
    ) : TmdbDiscover {

        override fun buildParameters(): Map<String, String?> {
            val params = newParameterMap()
            params[DiscoverParam.SORT_BY] = sortBy.value + "." + sortOrder.value

            if (includeAdult) {
                params[DiscoverParam.Movie.INCLUDE_ADULT] = includeAdult.toString()
            }

            if (includeVideo) {
                params[DiscoverParam.Movie.INCLUDE_VIDEO] = includeVideo.toString()
            }

            language?.let { params[DiscoverParam.LANGUAGE] = it }
            region?.let { params[DiscoverParam.Movie.REGION] = it }

            certification?.let { params[DiscoverParam.Movie.CERTIFICATION] = it }
            certificationGte?.let { params[DiscoverParam.Movie.CERTIFICATION_GTE] = it }
            certificationLte?.let { params[DiscoverParam.Movie.CERTIFICATION_LTE] = it }
            certificationCountry?.let { params[DiscoverParam.Movie.CERTIFICATION_COUNTRY] = it }

            withReleaseTypes?.let { f ->
                params[DiscoverParam.Movie.WITH_RELEASE_TYPE] = f.build { it.value.toString() }
            }

            withCast?.let { f ->
                params[DiscoverParam.Movie.WITH_CAST] = f.build { it }
            }

            withCrew?.let { f ->
                params[DiscoverParam.Movie.WITH_CREW] = f.build { it }
            }

            withPeople?.let { f ->
                params[DiscoverParam.Movie.WITH_PEOPLE] = f.build { it }
            }

            withKeywords?.let { f ->
                params[DiscoverParam.WITH_KEYWORDS] = f.build { it }
            }

            withoutKeywords?.let { f ->
                params[DiscoverParam.WITHOUT_KEYWORDS] = f.build { it }
            }

            withCompanies?.let { f ->
                params[DiscoverParam.WITH_COMPANIES] = f.build { it.toString() }
            }

            withoutCompanies?.let { f ->
                params[DiscoverParam.WITHOUT_COMPANIES] = f.build { it.toString() }
            }

            withOriginCountries?.let { f ->
                params[DiscoverParam.WITH_ORIGIN_COUNTRY] = f.build { it }
            }

            withOriginalLanguage?.let { params[DiscoverParam.Movie.WITH_ORIGINAL_LANGUAGE] = it }

            withRuntimeGte?.let { params[DiscoverParam.WITH_RUNTIME_GTE] = it.toString() }
            withRuntimeLte?.let { params[DiscoverParam.WITH_RUNTIME_LTE] = it.toString() }

            withWatchProviders?.let { f ->
                params[DiscoverParam.WITH_WATCH_PROVIDERS] = f.build { it.toString() }
            }

            withoutWatchProviders?.let { f ->
                params[DiscoverParam.WITHOUT_WATCH_PROVIDERS] = f.build { it.toString() }
            }

            when (releaseDate) {
                is TmdbDiscoverTimeRange.BetweenYears -> {
                    params[DiscoverParam.Movie.RELEASE_DATE_GTE] = releaseDate.firstDayOfYear
                    params[DiscoverParam.Movie.RELEASE_DATE_LTE] = releaseDate.lastDayOfYear
                }

                is TmdbDiscoverTimeRange.OneYear -> {
                    params[DiscoverParam.Movie.YEAR] = releaseDate.year.toString()
                }

                is TmdbDiscoverTimeRange.Custom -> {
                    params[DiscoverParam.Movie.RELEASE_DATE_GTE] = releaseDate.firstDate
                    params[DiscoverParam.Movie.RELEASE_DATE_LTE] = releaseDate.lastDate
                }

                else -> {
                    // do nothing
                }
            }

            when (primaryReleaseDate) {
                is TmdbDiscoverTimeRange.BetweenYears -> {
                    params[DiscoverParam.Movie.PRIMARY_RELEASE_DATE_GTE] = primaryReleaseDate.firstDayOfYear
                    params[DiscoverParam.Movie.PRIMARY_RELEASE_DATE_LTE] = primaryReleaseDate.lastDayOfYear
                }

                is TmdbDiscoverTimeRange.OneYear -> {
                    params[DiscoverParam.Movie.PRIMARY_RELEASE_YEAR] = primaryReleaseDate.year.toString()
                }

                is TmdbDiscoverTimeRange.Custom -> {
                    params[DiscoverParam.Movie.PRIMARY_RELEASE_DATE_GTE] = primaryReleaseDate.firstDate
                    params[DiscoverParam.Movie.PRIMARY_RELEASE_DATE_LTE] = primaryReleaseDate.lastDate
                }

                else -> {
                    // do nothing
                }
            }

            releaseYear?.let { params[DiscoverParam.Movie.YEAR] = it.toString() }
            primaryReleaseYear?.let { params[DiscoverParam.Movie.PRIMARY_RELEASE_YEAR] = it.toString() }

            return params
        }
    }

    data class Show(
        val includeAdult: Boolean = false,
        val includeNullFirstAirDates: Boolean = false,
        val sortBy: TmdbDiscoverShowSortBy = TmdbDiscoverShowSortBy.POPULARITY,
        override val sortOrder: TmdbSortOrder = TmdbSortOrder.DESC,
        override val voteAverageGte: Float? = null,
        override val voteAverageLte: Float? = null,
        override val voteCountGte: Int? = null,
        override val voteCountLte: Int? = null,
        override val withGenres: TmdbDiscoverFilter<String>? = null,
        override val withoutGenres: TmdbDiscoverFilter<String>? = null,
        val language: String? = null,
        val firstAirDate: TmdbDiscoverTimeRange? = null,
        val airDateGte: String? = null,
        val airDateLte: String? = null,
        val withNetworks: TmdbDiscoverFilter<Int>? = null,
        val withStatus: TmdbDiscoverFilter<TmdbShowStatus>? = null,
        val withCompanies: TmdbDiscoverFilter<Int>? = null,
        val withKeywords: TmdbDiscoverFilter<String>? = null,
        val withoutKeywords: TmdbDiscoverFilter<String>? = null,
        val withRuntimeGte: Int? = null,
        val withRuntimeLte: Int? = null,
        val withOriginalLanguage: String? = null,
        val withOriginCountries: TmdbDiscoverFilter<String>? = null,
        val withType: TmdbDiscoverFilter<Int>? = null,
        override val withWatchProviders: TmdbDiscoverFilter<Int>? = null,
        val withoutWatchProviders: TmdbDiscoverFilter<Int>? = null,
        val withoutCompanies: TmdbDiscoverFilter<Int>? = null,
        override val watchRegion: String? = null,
        override val withWatchMonetizationTypes: List<TmdbWatchMonetizationType> = emptyList(),
        val timezone: String? = null,
        val screenedTheatrically: Boolean? = null,
    ) : TmdbDiscover {

        override fun buildParameters(): Map<String, String?> {
            val params = newParameterMap()
            params[DiscoverParam.SORT_BY] = sortBy.value + "." + sortOrder.value

            if (includeAdult) {
                params[DiscoverParam.Movie.INCLUDE_ADULT] = includeAdult.toString()
            }

            if (includeNullFirstAirDates) {
                params[DiscoverParam.Show.INCLUDE_NULL_FIRST_AIR_DATES] = includeNullFirstAirDates.toString()
            }

            language?.let { params[DiscoverParam.LANGUAGE] = it }

            airDateGte?.let {
                params[DiscoverParam.Show.AIR_DATE_GTE] = airDateGte
            }
            airDateLte?.let {
                params[DiscoverParam.Show.AIR_DATE_LTE] = airDateLte
            }

            withNetworks?.let { f ->
                params[DiscoverParam.Show.WITH_NETWORKS] = f.build { it.toString() }
            }

            withStatus?.let { f ->
                params[DiscoverParam.Show.WITH_STATUS] = f.build { it.filterKey.toString() }
            }

            withCompanies?.let { f ->
                params[DiscoverParam.WITH_COMPANIES] = f.build { it.toString() }
            }

            withoutCompanies?.let { f ->
                params[DiscoverParam.WITHOUT_COMPANIES] = f.build { it.toString() }
            }

            withKeywords?.let { f ->
                params[DiscoverParam.WITH_KEYWORDS] = f.build { it }
            }

            withoutKeywords?.let { f ->
                params[DiscoverParam.WITHOUT_KEYWORDS] = f.build { it }
            }

            withRuntimeGte?.let { params[DiscoverParam.WITH_RUNTIME_GTE] = it.toString() }
            withRuntimeLte?.let { params[DiscoverParam.WITH_RUNTIME_LTE] = it.toString() }

            withOriginalLanguage?.let { params[DiscoverParam.Show.WITH_ORIGINAL_LANGUAGE] = it }

            withOriginCountries?.let { f ->
                params[DiscoverParam.WITH_ORIGIN_COUNTRY] = f.build { it }
            }

            withType?.let { f ->
                params[DiscoverParam.Show.WITH_TYPE] = f.build { it.toString() }
            }

            withWatchProviders?.let { f ->
                params[DiscoverParam.WITH_WATCH_PROVIDERS] = f.build { it.toString() }
            }

            withoutWatchProviders?.let { f ->
                params[DiscoverParam.WITHOUT_WATCH_PROVIDERS] = f.build { it.toString() }
            }

            timezone?.let { params[DiscoverParam.Show.TIMEZONE] = it }

            screenedTheatrically?.let { params[DiscoverParam.Show.SCREENED_THEATRICALLY] = it.toString() }

            when (firstAirDate) {
                is TmdbDiscoverTimeRange.BetweenYears -> {
                    params[DiscoverParam.Show.FIRST_AIR_DATE_GTE] = firstAirDate.firstDayOfYear
                    params[DiscoverParam.Show.FIRST_AIR_DATE_LTE] = firstAirDate.lastDayOfYear
                }

                is TmdbDiscoverTimeRange.OneYear -> {
                    params[DiscoverParam.Show.FIRST_AIR_DATE_YEAR] = firstAirDate.year.toString()
                }

                is TmdbDiscoverTimeRange.Custom -> {
                    params[DiscoverParam.Show.FIRST_AIR_DATE_GTE] = firstAirDate.firstDate
                    params[DiscoverParam.Show.FIRST_AIR_DATE_LTE] = firstAirDate.lastDate
                }

                else -> {
                    // do nothing
                }
            }

            return params
        }
    }
}

object DiscoverParam {

    const val SORT_BY = "sort_by"
    const val VOTE_COUNT_GTE = "vote_count.gte"
    const val VOTE_COUNT_LTE = "vote_count.lte"
    const val VOTE_AVERAGE_LTE = "vote_average.lte"
    const val VOTE_AVERAGE_GTE = "vote_average.gte"
    const val LANGUAGE = "language"
    const val WITH_GENRES = "with_genres"
    const val WITHOUT_GENRES = "without_genres"
    const val WITH_RUNTIME_GTE = "with_runtime.gte"
    const val WITH_RUNTIME_LTE = "with_runtime.lte"
    const val WITH_COMPANIES = "with_companies"
    const val WITHOUT_COMPANIES = "without_companies"
    const val WITH_KEYWORDS = "with_keywords"
    const val WITHOUT_KEYWORDS = "without_keywords"
    const val WITH_ORIGIN_COUNTRY = "with_origin_country"
    const val WITH_WATCH_PROVIDERS = "with_watch_providers"
    const val WITHOUT_WATCH_PROVIDERS = "without_watch_providers"
    const val WATCH_REGION = "watch_region"
    const val WITH_WATCH_MONETIZATION_TYPES = "with_watch_monetization_types"

    object Movie {
        const val REGION = "region"

        const val CERTIFICATION_COUNTRY = "certification_country"
        const val CERTIFICATION = "certification"
        const val CERTIFICATION_LTE = "certification.lte"
        const val CERTIFICATION_GTE = "certification.gte"
        const val INCLUDE_ADULT = "include_adult"
        const val INCLUDE_VIDEO = "include_video"

        const val PRIMARY_RELEASE_YEAR = "primary_release_year"
        const val PRIMARY_RELEASE_DATE_GTE = "primary_release_date.gte"
        const val PRIMARY_RELEASE_DATE_LTE = "primary_release_date.lte"
        const val RELEASE_DATE_GTE = "release_date.gte"
        const val RELEASE_DATE_LTE = "release_date.lte"
        const val WITH_RELEASE_TYPE = "with_release_type"
        const val YEAR = "year"

        const val WITH_CAST = "with_cast"
        const val WITH_CREW = "with_crew"
        const val WITH_PEOPLE = "with_people"
        const val WITH_ORIGINAL_LANGUAGE = "with_original_language"
    }

    object Show {

        const val AIR_DATE_GTE = "air_date.gte"
        const val AIR_DATE_LTE = "air_date.lte"

        const val FIRST_AIR_DATE_GTE = "first_air_date.gte"
        const val FIRST_AIR_DATE_LTE = "first_air_date.lte"
        const val FIRST_AIR_DATE_YEAR = "first_air_date_year"
        const val TIMEZONE = "timezone"

        const val WITH_NETWORKS = "with_networks"
        const val WITH_STATUS = "with_status"
        const val INCLUDE_NULL_FIRST_AIR_DATES = "include_null_first_air_dates"
        const val WITH_ORIGINAL_LANGUAGE = "with_original_language"
        const val SCREENED_THEATRICALLY = "screened_theatrically"
        const val WITH_TYPE = "with_type"
    }
}
