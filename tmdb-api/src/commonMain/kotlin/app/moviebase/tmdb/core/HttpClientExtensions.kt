package app.moviebase.tmdb.core

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.get
import io.ktor.util.pipeline.PipelinePhase

@Deprecated("Use directly the get method", ReplaceWith("get(urlString = paths.joinToString(separator = \"/\"), block = block).body()"))
internal suspend inline fun <reified T> HttpClient.getByPaths(
    vararg paths: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T = get(urlString = paths.joinToString(separator = "/"), block = block).body()


typealias RequestInterceptor = suspend (HttpRequestBuilder) -> Unit

internal fun HttpClient.interceptRequest(phase: PipelinePhase = HttpRequestPipeline.Render, interceptor: RequestInterceptor) =
    requestPipeline.intercept(phase) { interceptor(context) }
