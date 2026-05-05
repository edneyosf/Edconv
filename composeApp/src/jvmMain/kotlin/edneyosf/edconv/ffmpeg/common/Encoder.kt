package edneyosf.edconv.ffmpeg.common

import edneyosf.edconv.ffmpeg.av1.AV1Preset
import edneyosf.edconv.ffmpeg.h264.H264Preset
import edneyosf.edconv.ffmpeg.h265.H265Preset

enum class Encoder(
    val value: String, val text: String, val mediaType: MediaType,
    val compressions: List<CompressionType>, val defaultCRF: Int? = null, val minCRF: Int? = null,
    val maxCRF: Int? = null, val defaultVBR: Int? = null, val minVBR: Int? = null,
    val maxVBR: Int? = null, val defaultPreset: String? = null, val minPreset: Int? = null,
    val maxPreset: Int? = null, val defaultBitrate: Bitrate? = null,
    val defaultBitrateMono: Bitrate? = null, val defaultBitrateStereo: Bitrate? = null,
    val defaultBitrate51: Bitrate? = null, val defaultBitrate71: Bitrate? = null
) {
    OPUS(
        value = "libopus",
        text = "Opus",
        mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR),
        defaultBitrateMono = Bitrate.K32,
        defaultBitrateStereo = Bitrate.K160,
        defaultBitrate51 = Bitrate.K320,
        defaultBitrate71 = Bitrate.K512
    ),
    AAC(
        value = "aac",
        text = "AAC",
        mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR),
        defaultBitrateMono = Bitrate.K64,
        defaultBitrateStereo = Bitrate.K192,
        defaultBitrate51 = Bitrate.K448,
        defaultBitrate71 = Bitrate.K768
    ),
    AAC_FDK(
        value = "libfdk_aac",
        text = "FDK AAC",
        mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.VBR, CompressionType.CBR),
        defaultVBR = 5,
        minVBR = 1,
        maxVBR = 5,
        defaultBitrateMono = Bitrate.K64,
        defaultBitrateStereo = Bitrate.K192,
        defaultBitrate51 = Bitrate.K448,
        defaultBitrate71 = Bitrate.K768
    ),
    EAC3(
        value = "eac3",
        text = "E-AC3",
        mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR),
        defaultBitrateMono = Bitrate.K64,
        defaultBitrateStereo = Bitrate.K192,
        defaultBitrate51 = Bitrate.K448,
        defaultBitrate71 = Bitrate.K768
    ),
    AC3(
        value = "ac3",
        text = "AC3", mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR),
        defaultBitrateMono = Bitrate.K96,
        defaultBitrateStereo = Bitrate.K224,
        defaultBitrate51 = Bitrate.K640
    ),
    MP3(
        value = "libmp3lame",
        text = "MP3", mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR),
        defaultBitrateMono = Bitrate.K96,
        defaultBitrateStereo = Bitrate.K256
    ),
    FLAC(
        value = "flac",
        text = "FLAC",
        mediaType = MediaType.AUDIO,
        compressions = emptyList()
    ),
    AV1(
        value = "libsvtav1",
        text = "SVT-AV1",
        mediaType = MediaType.VIDEO,
        compressions = listOf(CompressionType.CRF, CompressionType.CBR),
        defaultCRF = 22,
        minCRF = 0,
        maxCRF = 63,
        defaultPreset = AV1Preset.P4.value,
        minPreset = 0,
        maxPreset = 13,
        defaultBitrate = Bitrate.M3
    ),
    H265(
        value = "libx265",
        text = "x265",
        mediaType = MediaType.VIDEO,
        compressions = listOf(CompressionType.CRF, CompressionType.CBR),
        defaultCRF = 21,
        minCRF = 0,
        maxCRF = 51,
        defaultPreset = H265Preset.SLOW.value,
        minPreset = 0,
        maxPreset = 9,
        defaultBitrate = Bitrate.M3_5
    ),
    H264(
        value = "libx264",
        text = "x264",
        mediaType = MediaType.VIDEO,
        compressions = listOf(CompressionType.CRF, CompressionType.CBR),
        defaultCRF = 19,
        minCRF = 0,
        maxCRF = 51,
        defaultPreset = H264Preset.SLOW.value,
        minPreset = 0,
        maxPreset = 9,
        defaultBitrate = Bitrate.M6
    );

    fun getVideoProfile(pixelFormat: PixelFormat?) = when(this) {
        H264 -> {
            if(pixelFormat == PixelFormat.BIT_8) "high"
            else "high10"
        }
        H265 -> {
            if(pixelFormat == PixelFormat.BIT_8) "main"
            else "main10"
        }
        AV1 -> "0"
        else -> null
    }

    fun toFileExtension() = when(this) {
        AAC, AAC_FDK -> "m4a"
        OPUS -> "opus"
        AC3 -> "ac3"
        MP3 -> "mp3"
        EAC3 -> "eac3"
        FLAC -> "flac"
        H264, H265 -> "mp4"
        AV1 -> "mkv"
    }

    fun presetValueByIndex(index: Int) = when(this) {
        H264 -> H264Preset.valueByIndex(index)
        H265 -> H265Preset.valueByIndex(index)
        AV1 -> AV1Preset.valueByIndex(index)
        else -> null
    }

    fun indexByPresetValue(value: String) = when(this) {
        H264 -> H264Preset.indexByValue(value)
        H265 -> H265Preset.indexByValue(value)
        AV1 -> AV1Preset.indexByValue(value)
        else -> null
    }

    fun fromAudioChannel(channels: Channels) = when (channels) {
        Channels.MONO -> defaultBitrateMono
        Channels.STEREO -> defaultBitrateStereo
        Channels.SURROUND_51 -> defaultBitrate51
        Channels.SURROUND_71 -> defaultBitrate71
    }

    companion object {
        fun getAll() = entries.toList()

        fun fromCodecName(name: String) = when(name.lowercase()) {
            "aac" -> AAC_FDK
            "opus" -> OPUS
            "mp3" -> MP3
            "ac3" -> AC3
            "eac3" -> EAC3
            "flac" -> FLAC
            "h264" -> H264
            "hevc" -> H265
            "av1" -> AV1
            else -> null
        }
    }
}