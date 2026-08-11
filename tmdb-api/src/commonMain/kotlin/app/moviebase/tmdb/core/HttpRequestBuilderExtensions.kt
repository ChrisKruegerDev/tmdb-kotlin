package app.moviebase.tmdb.core

import app.moviebase.tmdb.TmdbPaging
import app.moviebase.tmdb.TmdbWebConfig
import app.moviebase.tmdb.model.AppendResponse
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom

internal fun HttpRequestBuilder.json() {
    contentType(ContentType.Application.Json)
}

internal fun HttpRequestBuilder.endPointV3(vararg paths: String) {
    url {
        takeFrom(TmdbWebConfig.BASE_URL_TMDB)
        path(TmdbWebConfig.VERSION_PATH_V3, *paths)
    }
}

internal fun HttpRequestBuilder.endPointV4(vararg paths: String) {
    url {
        takeFrom(TmdbWebConfig.BASE_URL_TMDB)
        path(TmdbWebConfig.VERSION_PATH_V4, *paths)
    }
}

internal fun HttpRequestBuilder.parameters(parameters: Map<String, Any?>) {
    parameters.entries.forEach {
        parameter(it.key, it.value)
    }
}

internal fun HttpRequestBuilder.parameterLanguage(language: String?) {
    language?.let { parameter("language", it) }
}

internal fun HttpRequestBuilder.parameterIncludeImageLanguage(language: String?) {
    language?.let { parameter("include_image_language", it) }
}

internal fun HttpRequestBuilder.parameterIncludeVideoLanguage(language: String?) {
    language?.let { parameter("include_video_language", it) }
}

internal fun HttpRequestBuilder.parameterRegion(region: String?) {
    region?.let { parameter("region", it) }
}

internal fun HttpRequestBuilder.parameterPage(page: Int) {
    require(page in TmdbPaging.FIRST_PAGE..TmdbPaging.MAX_PAGE) {
        "invalid page: $page (expected ${TmdbPaging.FIRST_PAGE}..${TmdbPaging.MAX_PAGE})"
    }
    parameter("page", page)
}

internal fun HttpRequestBuilder.parameterAppendResponses(
    appendResponses: Iterable<AppendResponse>?,
    additional: List<String> = emptyList(),
) {
    val values = appendResponses?.map { it.value }.orEmpty() + additional
    if (values.isEmpty()) return
    parameter("append_to_response", values.joinToString(","))
}
