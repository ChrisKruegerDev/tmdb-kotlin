package app.moviebase.tmdb

import app.moviebase.tmdb.api.TmdbAccountApi
import app.moviebase.tmdb.api.TmdbAuthenticationApi
import app.moviebase.tmdb.api.TmdbCertificationsApi
import app.moviebase.tmdb.api.TmdbChangesApi
import app.moviebase.tmdb.api.TmdbCollectionsApi
import app.moviebase.tmdb.api.TmdbCompaniesApi
import app.moviebase.tmdb.api.TmdbConfigurationApi
import app.moviebase.tmdb.api.TmdbCreditsApi
import app.moviebase.tmdb.api.TmdbDiscoverApi
import app.moviebase.tmdb.api.TmdbFindApi
import app.moviebase.tmdb.api.TmdbGenresApi
import app.moviebase.tmdb.api.TmdbGuestSessionsApi
import app.moviebase.tmdb.api.TmdbKeywordsApi
import app.moviebase.tmdb.api.TmdbListsApi
import app.moviebase.tmdb.api.TmdbMoviesApi
import app.moviebase.tmdb.api.TmdbNetworksApi
import app.moviebase.tmdb.api.TmdbPeopleApi
import app.moviebase.tmdb.api.TmdbReviewsApi
import app.moviebase.tmdb.api.TmdbSearchApi
import app.moviebase.tmdb.api.TmdbShowApi
import app.moviebase.tmdb.api.TmdbShowEpisodeGroupsApi
import app.moviebase.tmdb.api.TmdbShowEpisodesApi
import app.moviebase.tmdb.api.TmdbShowSeasonsApi
import app.moviebase.tmdb.api.TmdbTrendingApi
import app.moviebase.tmdb.core.HttpClientFactory
import app.moviebase.tmdb.core.interceptRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

fun Tmdb3(block: TmdbClientConfig.() -> Unit): Tmdb3 {
    val config = TmdbClientConfig().apply(block)
    return Tmdb3(config)
}

class Tmdb3 internal constructor(private val config: TmdbClientConfig) {

    constructor(tmdbApiKey: String) : this(TmdbClientConfig.withKey(tmdbApiKey))

    init {
        requireNotNull(config.tmdbApiKey) {
            "TMDB API key unavailable. Set the tmdbApiKey field in the class TmdbClientConfig when instantiate the TMDB client."
        }
    }

    private val client: HttpClient by lazy {
        HttpClientFactory.buildHttpClient(TmdbVersion.V3, config).apply {
            interceptRequest {
                it.parameter(TmdbUrlParameter.API_KEY, config.tmdbApiKey)

                val pathSegments = it.url.pathSegments.toSet()
                if (pathSegments.contains("account") || pathSegments.contains("authentication")) {
                    config.tmdbAuthCredentials?.sessionIdProvider?.invoke()?.let { sessionId ->
                        it.parameter(TmdbUrlParameter.SESSION_ID, sessionId)
                    }
                }

                config.tmdbAuthCredentials?.guestSessionIdProvider?.invoke()?.let { sessionId ->
                    it.parameter(TmdbUrlParameter.GUEST_SESSION_ID, sessionId)
                }
            }
        }
    }

    val account: TmdbAccountApi by buildApi(::TmdbAccountApi)
    val authentication by buildApi(::TmdbAuthenticationApi)
    val certifications by buildApi(::TmdbCertificationsApi)
    val changes by buildApi(::TmdbChangesApi)
    val collections by buildApi(::TmdbCollectionsApi)
    val companies by buildApi(::TmdbCompaniesApi)
    val configuration by buildApi(::TmdbConfigurationApi)
    val credits by buildApi(::TmdbCreditsApi)
    val discover by buildApi(::TmdbDiscoverApi)
    val find by buildApi(::TmdbFindApi)
    val genres by buildApi(::TmdbGenresApi)
    val guestSessions by buildApi(::TmdbGuestSessionsApi)
    val keywords by buildApi(::TmdbKeywordsApi)
    val lists by buildApi(::TmdbListsApi)
    val movies by buildApi(::TmdbMoviesApi)
    val networks by buildApi(::TmdbNetworksApi)
    val trending by buildApi(::TmdbTrendingApi)
    val people by buildApi(::TmdbPeopleApi)
    val reviews by buildApi(::TmdbReviewsApi)
    val search by buildApi(::TmdbSearchApi)
    val show by buildApi(::TmdbShowApi)
    val showSeasons by buildApi(::TmdbShowSeasonsApi)
    val showEpisodes by buildApi(::TmdbShowEpisodesApi)
    val showEpisodeGroups by buildApi(::TmdbShowEpisodeGroupsApi)

    private inline fun <T> buildApi(crossinline builder: (HttpClient) -> T) = lazy {
        builder(client)
    }
}
