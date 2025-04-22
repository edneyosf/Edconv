package edneyosf.edconv.edconv.core.data

import kotlinx.serialization.Serializable

@Serializable
data class SubtitleData(
    val codec: String? = null,
    val title: String? = null,
    val language: String? = null
)