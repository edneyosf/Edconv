package edneyosf.edconv.edconv.av1

enum class AV1Preset(val value: String) {
    P0(value = "0"),
    P1(value = "1"),
    P2(value = "2"),
    P3(value = "3"),
    P4(value = "4"),
    P5(value = "5"),
    P6(value = "6"),
    P7(value = "7"),
    P8(value = "8"),
    P9(value = "9"),
    P10(value = "10"),
    P11(value = "11"),
    P12(value = "12"),
    P13(value = "13");

    companion object {
        fun valueByIndex(index: Int) = entries.find { it.ordinal == index }?.value
        fun indexByValue(value: String) = entries.find { it.value == value }?.ordinal
    }
}