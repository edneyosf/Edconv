package edneyosf.edconv.ffmpeg.data

import kotlinx.serialization.Serializable

@Serializable
data class ProgressData(
    val time: Long, // milliseconds
    val speed: String
)