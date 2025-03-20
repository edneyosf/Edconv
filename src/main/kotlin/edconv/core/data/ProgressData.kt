package edconv.core.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ProgressData(
    val size: String,
    val time: String,
    val bitrate: String,
    val speed: String
) {
    companion object {
        fun fromJsonString(json: String): ProgressData = Json.decodeFromString(json)
    }
}