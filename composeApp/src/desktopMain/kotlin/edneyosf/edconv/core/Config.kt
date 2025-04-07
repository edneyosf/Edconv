package edneyosf.edconv.core

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val ffmpegPath: String = "",
    val ffprobePath: String = ""
)