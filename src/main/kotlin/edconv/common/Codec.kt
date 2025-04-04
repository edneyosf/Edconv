package edconv.common

enum class Codec(
    val value: String, val text: String, val mediaType: MediaType, val compression: CompressionType,
    val minCrf: Int? = null, val maxCrf: Int? = null, val minVbr: Int? = null, val maxVbr: Int? = null
) {
    AAC(
        value = "aac",
        text = "AAC",
        mediaType = MediaType.AUDIO,
        compression = CompressionType.CONSTANT
    ),
    AAC_FDK(
        value = "libfdk_aac",
        text = "AAC (FDK)",
        mediaType = MediaType.AUDIO,
        compression = CompressionType.CONSTANT_VARIABLE,
        minVbr = 1,
        maxVbr = 5
    ),
    OPUS(
        value = "libopus",
        text = "Opus",
        mediaType = MediaType.AUDIO,
        compression = CompressionType.CONSTANT
    ),
    AC3(
        value = "ac3",
        text = "AC3", mediaType = MediaType.AUDIO,
        compression = CompressionType.CONSTANT
    ),
    EAC3(
        value = "eac3",
        text = "EAC3",
        mediaType = MediaType.AUDIO,
        compression = CompressionType.CONSTANT
    ),
    FLAC(
        value = "flac",
        text = "FLAC",
        mediaType = MediaType.AUDIO,
        compression = CompressionType.VARIABLE
    ),

    H264(
        value = "libx264",
        text = "H.264 (x264)",
        mediaType = MediaType.VIDEO,
        compression = CompressionType.CONSTANT_VARIABLE,
        minCrf = 0,
        maxCrf = 51
    ),
    H265(
        value = "libx265",
        text = "H.265 (x265)",
        mediaType = MediaType.VIDEO,
        compression = CompressionType.CONSTANT_VARIABLE,
        minCrf = 0,
        maxCrf = 51
    ),
    VP9(
        value = "libvpx-vp9",
        text = "VP9",
        mediaType = MediaType.VIDEO,
        compression = CompressionType.CONSTANT_VARIABLE,
        minCrf = 0,
        maxCrf = 63
    ),
    AV1(
        value = "libsvtav1",
        text = "AV1 (SVT)",
        mediaType = MediaType.VIDEO,
        compression = CompressionType.CONSTANT_VARIABLE,
        minCrf = 0,
        maxCrf = 63
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