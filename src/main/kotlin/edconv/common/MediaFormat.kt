package edconv.common

enum class MediaFormat(val codec: String, val text: String) {
    AAC("aac", "AAC"),
    EAC3("eac3", "E-AC3"),
    H265("h265", "H.265 (x265)"),
    AV1("av1", "AV1 (SVT)");

    companion object {
        fun fromString(it: String) = when(it.lowercase()) {
            "aac" -> AAC
            "eac3" -> EAC3
            "h265" -> H265
            "av1" -> AV1
            else -> null
        }
    }
}