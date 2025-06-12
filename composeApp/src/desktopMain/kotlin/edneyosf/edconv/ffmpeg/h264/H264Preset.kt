package edneyosf.edconv.ffmpeg.h264

enum class H264Preset(val value: String) {
    ULTRAFAST(value = "ultrafast"),
    SUPERFAST(value = "superfast"),
    VERYFAST(value = "veryfast"),
    FASTER(value = "faster"),
    FAST(value = "fast"),
    MEDIUM(value = "medium"),
    SLOW(value = "slow"),
    SLOWER(value = "slower"),
    VERYSLOW(value = "veryslow"),
    PLACEBO(value = "placebo");

    companion object {
        fun valueByIndex(index: Int) = entries.find { it.ordinal == index }?.value
        fun indexByValue(value: String) = entries.find { it.value == value }?.ordinal
    }
}