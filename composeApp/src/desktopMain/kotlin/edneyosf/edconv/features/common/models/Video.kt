package edneyosf.edconv.features.common.models

data class Video(
    val codec: String? = null,
    val title: String? = null,
    val language: String? = null,
    val profile: String? = null,
    val width: Int,
    val height: Int,
    val bitDepth: Int? = null,
    val pixFmt: String? = null,
    val fps: Float? = null,
    val level: Int? = null,
    val filmGrain: Boolean = false,
    val displayAspectRatio: String? = null,
    val fieldOrder: String? = null
)