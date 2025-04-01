package edconv.core.data

import kotlinx.serialization.Serializable

@Serializable
data class ProgressData(
    val size: String,
    val time: Long, // milliseconds
    val bitrate: String,
    val speed: String
)