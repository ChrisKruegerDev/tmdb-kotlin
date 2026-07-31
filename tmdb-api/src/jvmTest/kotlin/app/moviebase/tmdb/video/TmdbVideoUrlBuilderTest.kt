package app.moviebase.tmdb.video

import app.moviebase.tmdb.model.TmdbVideo
import app.moviebase.tmdb.model.TmdbVideoSite
import app.moviebase.tmdb.url.TmdbUrlBuilder
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class TmdbVideoUrlBuilderTest {

    @Test
    fun `Should return a proper YouTube url`() {
        val tmdbVideo = TmdbVideo(id = "123", key = "qwerasdf", site = TmdbVideoSite.YOUTUBE)

        val videoUrl = TmdbUrlBuilder.buildVideo(tmdbVideo)

        assertThat(videoUrl).isEqualTo("https://www.youtube.com/watch?v=qwerasdf")
    }

    @Test
    fun `Should return a proper Vimeo url`() {
        val tmdbVideo = TmdbVideo(id = "123", key = "qwerasdf", site = TmdbVideoSite.VIMEO)

        val videoUrl = TmdbUrlBuilder.buildVideo(tmdbVideo)

        assertThat(videoUrl).isEqualTo("https://vimeo.com/qwerasdf")
    }
}
