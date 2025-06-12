package edneyosf.edconv.ffmpeg.data

import kotlinx.serialization.Serializable

@Serializable
data class AudioData(
    val codec: String? = null,
    val title: String? = null,
    val language: String? = null,
    val profile: String? = null,
    val channels: Int? = null,
    val sampleRate: Int? = null,
    val bitDepth: Int? = null,
    val bitRate: Long? = null
)