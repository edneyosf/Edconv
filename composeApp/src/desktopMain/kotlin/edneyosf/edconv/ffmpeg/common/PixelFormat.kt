package edneyosf.edconv.ffmpeg.common

enum class PixelFormat(val value: String, val text: String) {
    BIT_8(value = "yuv420p", text = "8-bit"),
    BIT_10(value = "yuv420p10le", text = "10-bit");

    companion object {
        fun getAll() = entries.toList()

        fun fromValue(value: Int?) = when(value) {
            8 -> BIT_8
            10 -> BIT_10
            else -> null
        }

        fun fromValue(value: String?) = when(value) {
            BIT_8.value -> BIT_8
            BIT_10.value -> BIT_10
            else -> null
        }
    }
}