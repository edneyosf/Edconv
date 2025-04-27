package edneyosf.edconv.features.converter.domain.model

data class Audio(
    val codec: String? = null,
    val title: String? = null,
    val language: String? = null,
    val profile: String? = null,
    val channels: Int,
    val sampleRate: Int? = null,
    val bitDepth: Int? = null
)