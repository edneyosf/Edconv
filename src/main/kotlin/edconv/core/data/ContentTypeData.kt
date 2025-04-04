package edconv.core.data

import kotlinx.serialization.Serializable

@Serializable
data class ContentTypeData(
    val audio: Boolean = false,
    val video: Boolean = false,
    val subtitle: Boolean = false
)