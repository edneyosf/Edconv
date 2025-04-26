package edneyosf.edconv.ffmpeg.data

import edneyosf.edconv.ffmpeg.common.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class InputMedia(
    val path: String,
    val type: MediaType,
    val contentType: ContentTypeData,
    val size: Long,
    val formatName: String? = null,
    val duration: Long? = null,
    val bitRate: Long? = null,
    val videoStreams: List<VideoData> = emptyList(),
    val audioStreams: List<AudioData> = emptyList(),
    val subtitleStreams: List<SubtitleData> = emptyList()
)