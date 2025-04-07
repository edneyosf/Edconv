package edneyosf.edconv.edconv.common

enum class PixelFormat(val value: String, val text: String) {
    BIT_8(value = "yuv420p", text = "8-bit"),
    BIT_10(value = "yuv420p10le", text = "10-bit");

    companion object {
        fun getAll() = entries.toList()
    }
}