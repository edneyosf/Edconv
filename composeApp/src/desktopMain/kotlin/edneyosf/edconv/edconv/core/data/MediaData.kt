package edneyosf.edconv.edconv.core.data

import edneyosf.edconv.edconv.common.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class MediaData(
    val path: String,
    val type: MediaType,
    val contentType: ContentTypeData,
    val duration: Long,
    val channels: Int? = null,
    val resolution: Pair<Int, Int>? = null,
    val size: Long
)