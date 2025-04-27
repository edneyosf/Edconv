package edneyosf.edconv.ffmpeg.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormatData(
    @SerialName("format_name")
    val formatName: String? = null,
    @SerialName("format_long_name")
    val formatLongName: String? = null,
    @SerialName("bit_rate")
    val bitRate: Long? = null,
    val duration: Float? = null
)