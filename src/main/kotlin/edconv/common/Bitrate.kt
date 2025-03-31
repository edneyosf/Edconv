package edconv.common

enum class Bitrate(val value: String, val text: String) {
    K32(value = "32k", text = "32 kbps"),
    K48(value = "48k", text = "48 kbps"),
    K64(value = "64k", text = "64 kbps"),
    K96(value = "96k", text = "96 kbps"),
    K128(value = "128k", text = "128 kbps"),
    K160(value = "160k", text = "160 kbps"),
    K192(value = "192k", text = "192 kbps"),
    K224(value = "224k", text = "224 kbps"),
    K256(value = "256k", text = "256 kbps"),
    K320(value = "320k", text = "320 kbps"),
    K384(value = "384k", text = "384 kbps"),
    K448(value = "448k", text = "448 kbps"),
    K512(value = "512k", text = "512 kbps"),
    K640(value = "640k", text = "640 kbps"),
    K768(value = "768k", text = "768 kbps"),
    K960(value = "960k", text = "960 kbps");

    companion object {
        fun getAll() = entries.toList()
    }
}