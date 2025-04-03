package edconv.core.data

import edconv.common.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class MediaData(
    val path: String,
    val type: MediaType,
    val duration: Long,
    val channels: Int? = null,
    val resolution: Pair<Int, Int>? = null,
    val size: Long
)