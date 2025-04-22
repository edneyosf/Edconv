package edneyosf.edconv.edconv.ffprobe

import kotlinx.serialization.Serializable

@Serializable
data class StreamTagsFFprobe(
    val language: String? = null,
    val title: String? = null
)