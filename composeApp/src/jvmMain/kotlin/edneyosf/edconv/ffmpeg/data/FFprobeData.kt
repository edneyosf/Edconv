package edneyosf.edconv.ffmpeg.data

import kotlinx.serialization.Serializable

@Serializable
data class FFprobeData(val format: FormatData, val streams: List<StreamData>)