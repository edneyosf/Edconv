package edneyosf.edconv.edconv.core.data

import kotlinx.serialization.Serializable

@Serializable
data class VideoData(
    val codec: String? = null,
    val title: String? = null,
    val language: String? = null,
    val profile: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val bitDepth: Int? = null,
    val pixFmt: String? = null,
    val fps: Float? = null,
    val level: Int? = null,
    val filmGrain: Boolean = false,
    val displayAspectRatio: String? = null,
    val fieldOrder: String? = null
)