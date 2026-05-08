package edneyosf.edconv.features.common.models

data class Audio(
    val codecName: String? = null,
    val codecLongName: String? = null,
    val title: String? = null,
    val language: String? = null,
    val profile: String? = null,
    val channels: Int,
    val sampleRate: Int? = null,
    val bitDepth: Int? = null,
    val bitRate: Long? = null,
    val bitRateText: String? = null
)