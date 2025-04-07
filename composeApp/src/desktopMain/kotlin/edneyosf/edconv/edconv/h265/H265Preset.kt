package edneyosf.edconv.edconv.h265

enum class H265Preset(val id: Int, val value: String) {
    ULTRAFAST(id = 0, value = "ultrafast"),
    SUPERFAST(id = 1, value = "superfast"),
    VERYFAST(id = 2, value = "veryfast"),
    FASTER(id = 3, value = "faster"),
    FAST(id = 4, value = "fast"),
    MEDIUM(id = 5, value = "medium"),
    SLOW(id = 6, value = "slow"),
    SLOWER(id = 7, value = "slower"),
    VERYSLOW(id = 8, value = "veryslow");

    companion object {
        const val MIN_ID = 0
        const val MAX_ID = 8

        fun fromId(id: Int) = entries.find { it.id == id }
    }
}