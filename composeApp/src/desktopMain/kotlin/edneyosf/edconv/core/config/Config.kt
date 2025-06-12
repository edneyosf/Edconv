package edneyosf.edconv.core.config

import kotlinx.serialization.Serializable

@Serializable
data class  Config(
    val ffmpegPath: String = "",
    val ffprobePath: String = "",
    val vmafModelPath: String = ""
)