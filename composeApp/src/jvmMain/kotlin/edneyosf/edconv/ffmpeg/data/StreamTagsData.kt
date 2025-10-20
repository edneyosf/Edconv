package edneyosf.edconv.ffmpeg.data

import kotlinx.serialization.Serializable

@Serializable
data class StreamTagsData(
    val language: String? = null,
    val title: String? = null
)