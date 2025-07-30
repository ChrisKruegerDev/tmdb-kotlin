package app.moviebase.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbEpisodeGroup(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("episode_count") val episodeCount: Int,
    @SerialName("group_count") val groupCount: Int,
    @SerialName("type") val type: Int
)

@Serializable
data class TmdbScreenedTheatrically(
    @SerialName("id") val id: Int,
    @SerialName("episode_number") val episodeNumber: Int,
    @SerialName("season_number") val seasonNumber: Int
)
