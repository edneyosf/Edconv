package edneyosf.edconv.ffmpeg.ffprobe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamFFprobe(
    @SerialName("codec_type")
    val codecType: String? = null,
    @SerialName("codec_name")
    val codecName: String? = null,
    @SerialName("codec_long_name")
    val codecLongName: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val level: Int? = null,
    @SerialName("film_grain")
    val filmGrain: Int? = null,
    @SerialName("bits_per_raw_sample")
    val bitDepth: Int? = null,
    @SerialName("sample_rate")
    val sampleRate: Int? = null,
    val channels: Int? = null,
    @SerialName("r_frame_rate")
    val frameRate: String? = null,
    @SerialName("pix_fmt")
    val pixFmt: String? = null,
    val profile: String? = null,
    @SerialName("field_order")
    val fieldOrder: String? = null,
    @SerialName("display_aspect_ratio")
    val displayAspectRatio: String? = null,
    val tags: StreamTagsFFprobe? = null
)