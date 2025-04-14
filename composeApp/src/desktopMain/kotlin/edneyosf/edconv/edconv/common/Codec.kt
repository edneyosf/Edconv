package edneyosf.edconv.edconv.common

enum class Codec(
    val value: String, val text: String, val mediaType: MediaType, val compressions: List<CompressionType>,
    val defaultCRF: Int? = null, val minCRF: Int? = null, val maxCRF: Int? = null, val defaultVBR: Int? = null,
    val minVBR: Int? = null, val maxVBR: Int? = null
) {
    AAC(
        value = "aac",
        text = "AAC",
        mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR),
    ),
    AAC_FDK(
        value = "libfdk_aac",
        text = "AAC (FDK)",
        mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.VBR, CompressionType.CBR),
        defaultVBR = 5,
        minVBR = 1,
        maxVBR = 5
    ),
    OPUS(
        value = "libopus",
        text = "Opus",
        mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR)
    ),
    AC3(
        value = "ac3",
        text = "AC3", mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR)
    ),
    EAC3(
        value = "eac3",
        text = "EAC3",
        mediaType = MediaType.AUDIO,
        compressions = listOf(CompressionType.CBR)
    ),
    FLAC(
        value = "flac",
        text = "FLAC",
        mediaType = MediaType.AUDIO,
        compressions = emptyList()
    ),

    H264(
        value = "libx264",
        text = "H.264 (x264)",
        mediaType = MediaType.VIDEO,
        compressions = listOf(CompressionType.CRF, CompressionType.CBR),
        defaultCRF = 20,
        minCRF = 0,
        maxCRF = 51
    ),
    H265(
        value = "libx265",
        text = "H.265 (x265)",
        mediaType = MediaType.VIDEO,
        compressions = listOf(CompressionType.CRF, CompressionType.CBR),
        defaultCRF = 21,
        minCRF = 0,
        maxCRF = 51
    ),
    VP9(
        value = "libvpx-vp9",
        text = "VP9",
        mediaType = MediaType.VIDEO,
        compressions = listOf(CompressionType.CRF, CompressionType.CBR),
        defaultCRF = 19,
        minCRF = 0,
        maxCRF = 63
    ),
    AV1(
        value = "libsvtav1",
        text = "AV1 (SVT)",
        mediaType = MediaType.VIDEO,
        compressions = listOf(CompressionType.CRF, CompressionType.CBR),
        defaultCRF = 25,
        minCRF = 0,
        maxCRF = 63
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
        EAC3 -> "eac3"
        FLAC -> "flac"

        VP9 -> "webm"
        H264, H265, AV1 -> "mkv"
    }

    companion object {
        fun getAll() = entries.toList()
    }
}