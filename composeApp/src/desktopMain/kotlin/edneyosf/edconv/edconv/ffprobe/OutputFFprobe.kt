package edneyosf.edconv.edconv.ffprobe

import kotlinx.serialization.Serializable

@Serializable
data class OutputFFprobe(val format: FormatFFprobe, val streams: List<StreamFFprobe>)