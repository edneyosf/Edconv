package edconv.common

private const val AAC_ = "aac"
private const val EAC3_ = "eac3"
private const val H265_ = "h265"
private const val AV1_ = "av1"

enum class MediaFormat(val codec: String) {
    AAC(AAC_),
    EAC3(EAC3_),
    H265(H265_),
    AV1(AV1_);

    companion object {
        fun fromString(it: String) = when(it.lowercase()) {
            AAC_ -> AAC
            EAC3_ -> EAC3
            H265_ -> H265
            AV1_ -> AV1
            else -> null
        }
    }
}