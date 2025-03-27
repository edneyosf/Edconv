package edconv.common

enum class Kbps(val value: String, val text: String) {
    K32(value = "32", text = "32 kbps"),
    K48(value = "48", text = "48 kbps"),
    K64(value = "64", text = "64 kbps"),
    K96(value = "96", text = "96 kbps"),
    K128(value = "128", text = "128 kbps"),
    K160(value = "160", text = "160 kbps"),
    K192(value = "192", text = "192 kbps"),
    K224(value = "224", text = "224 kbps"),
    K256(value = "256", text = "256 kbps"),
    K320(value = "320", text = "320 kbps"),
    K384(value = "384", text = "384 kbps"),
    K448(value = "448", text = "448 kbps"),
    K512(value = "512", text = "512 kbps"),
    K640(value = "640", text = "640 kbps"),
    K768(value = "768", text = "768 kbps"),
    K960(value = "960", text = "960 kbps");

    companion object {
        fun getAll() = entries.toList()
    }
}