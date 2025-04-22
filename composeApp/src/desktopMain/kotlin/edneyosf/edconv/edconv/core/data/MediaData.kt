package edneyosf.edconv.edconv.core.data

import edneyosf.edconv.edconv.common.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class MediaData(
    val path: String,
    val type: MediaType,
    val formatName: String? = null,
    val contentType: ContentTypeData,
    val duration: Long? = null,
    val bitRate: Long? = null,
    val size: Long,
    val videoStreams: List<VideoData> = emptyList(),
    val audioStreams: List<AudioData> = emptyList(),
    val subtitleStreams: List<SubtitleData> = emptyList()
)