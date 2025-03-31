package edconv.core.data

import edconv.common.MediaType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class MediaData(
    val path: String, val type: MediaType, val duration: Long, val resolution: Pair<Int, Int>? = null, val size: Long
) {
    companion object {
        fun fromJsonString(json: String): MediaData = Json.decodeFromString(json)
    }
}