package edconv.core.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ProgressData(
    val size: String,
    val time: Long,   // Tempo processado (HH:MM:SS.ms)
    val bitrate: String, // Bitrate em kbps
    val speed: String // Velocidade de processamento
) {
    companion object {
        fun fromJsonString(json: String): ProgressData = Json.decodeFromString(json)
    }
}