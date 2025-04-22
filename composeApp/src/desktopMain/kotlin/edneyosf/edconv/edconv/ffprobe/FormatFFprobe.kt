package edneyosf.edconv.edconv.ffprobe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormatFFprobe(
    @SerialName("format_name")
    val formatName: String? = null,
    @SerialName("format_long_name")
    val formatLongName: String? = null,
    @SerialName("bit_rate")
    val bitRate: Long? = null,
    val duration: Float? = null
)