package app.moviebase.tmdb.core

import app.moviebase.tmdb.model.TmdbSeasonDetail
import app.moviebase.tmdb.model.TmdbShowDetail
import app.moviebase.tmdb.model.TmdbShowDetailWithSeasons
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

private val SEASON_KEY = Regex("""^season/(\d+)$""")

/**
 * TMDB returns appended seasons as dynamic top-level keys (`season/1`, `season/2`), which cannot be
 * declared as fields. Decode the payload once, read [TmdbShowDetail] from the same object, then pull
 * out whichever `season/N` keys are present.
 */
internal class TmdbShowDetailWithSeasonsSerializer : KSerializer<TmdbShowDetailWithSeasons> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("TmdbShowDetailWithSeasons")

    override fun deserialize(decoder: Decoder): TmdbShowDetailWithSeasons {
        val input = decoder as? JsonDecoder
            ?: throw IllegalStateException("TmdbShowDetailWithSeasons requires a JSON decoder")
        val root = input.decodeJsonElement().jsonObject

        val show = input.json.decodeFromJsonElement(TmdbShowDetail.serializer(), root)
        val seasons = buildMap {
            for ((key, value) in root) {
                val seasonNumber = SEASON_KEY.matchEntire(key)?.groupValues?.get(1)?.toIntOrNull() ?: continue
                put(seasonNumber, input.json.decodeFromJsonElement(TmdbSeasonDetail.serializer(), value))
            }
        }

        return TmdbShowDetailWithSeasons(show = show, seasons = seasons)
    }

    override fun serialize(encoder: Encoder, value: TmdbShowDetailWithSeasons) {
        throw UnsupportedOperationException("TmdbShowDetailWithSeasons is read-only")
    }
}
