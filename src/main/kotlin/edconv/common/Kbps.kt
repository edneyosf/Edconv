package edconv.common

enum class Kbps(val value: String) {
    K32(value = "32"),
    K48(value = "48"),
    K64(value = "64"),
    K96(value = "96"),
    K128(value = "128"),
    K160(value = "160"),
    K192(value = "192"),
    K224(value = "224"),
    K256(value = "256"),
    K320(value = "320"),
    K384(value = "384"),
    K448(value = "448"),
    K512(value = "512"),
    K640(value = "640"),
    K768(value = "768"),
    K960(value = "960");

    companion object {
        fun getAll() = entries.toList()
    }
}