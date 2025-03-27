package edconv.common

enum class MediaFormat(
    val codec: String, val text: String, val isVideo: Boolean, val minCrf: Int? = null, val maxCrf: Int? = null,
    val minVbr: Int? = null, val maxVbr: Int? = null
) {
    AAC(codec = "aac", text = "AAC", isVideo = false, minVbr = 1, maxVbr = 5),
    EAC3(codec = "eac3", text = "E-AC3", isVideo = false),
    H265(codec = "h265", text = "H.265 (x265)", isVideo = true, minCrf = 0, maxCrf = 51),
    AV1(codec = "av1", text = "AV1 (SVT)", isVideo = true, minCrf = 0, maxCrf = 63);

    fun toFileExtension() = when(this) {
        AAC -> "aac"
        EAC3 -> "eac3"
        H265, AV1 -> "mkv"
    }

    companion object {
        fun getAll() = entries.toList()
    }
}