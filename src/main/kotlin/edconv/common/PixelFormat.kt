package edconv.common

enum class PixelFormat(val value: String, val text: String) {
    BIT_8(value = "8", text = "8-bit"),
    BIT_10(value = "10", text = "10-bit");

    companion object {
        fun getAll() = entries.toList()
    }
}