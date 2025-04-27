package edneyosf.edconv.features.common.models

import edneyosf.edconv.ffmpeg.common.MediaType

data class InputMedia(
    val path: String,
    val type: MediaType,
    val size: Long,
    val sizeText: String,
    val formatName: String? = null,
    val duration: Long,
    val durationText: String,
    val bitRate: Long? = null,
    val bitrateText: String? = null,
    val videos: List<Video> = emptyList(),
    val audios: List<Audio> = emptyList(),
    val subtitles: List<Subtitle> = emptyList()
)