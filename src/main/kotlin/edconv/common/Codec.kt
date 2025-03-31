package edconv.common

enum class Codec(
    val value: String, val text: String, val isVideo: Boolean, val minCrf: Int? = null, val maxCrf: Int? = null,
    val minVbr: Int? = null, val maxVbr: Int? = null
) {
    AAC(value = "aac", text = "AAC", isVideo = false),
    AAC_FDK(value = "libfdk_aac", text = "AAC (FDK)", isVideo = false, minVbr = 1, maxVbr = 5),
    EAC3(value = "eac3", text = "E-AC3", isVideo = false),
    H265(value = "libx265", text = "H.265 (x265)", isVideo = true, minCrf = 0, maxCrf = 51),
    AV1(value = "libsvtav1", text = "AV1 (SVT)", isVideo = true, minCrf = 0, maxCrf = 63);

    fun toFileExtension() = when(this) {
        AAC, AAC_FDK -> "m4a"
        EAC3 -> "eac3"
        H265, AV1 -> "mkv"
    }

    companion object {
        fun getAll() = entries.toList()
    }
}