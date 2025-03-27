package edconv.av1

enum class AV1Preset(val id: Int, val value: String) {
    P0(id = 0, value = "0"),
    P1(id = 1, value = "1"),
    P2(id = 2, value = "2"),
    P3(id = 3, value = "3"),
    P4(id = 4, value = "4"),
    P5(id = 5, value = "5"),
    P6(id = 6, value = "6"),
    P7(id = 7, value = "7"),
    P8(id = 8, value = "8"),
    P9(id = 9, value = "9"),
    P10(id = 10, value = "10"),
    P11(id = 11, value = "11"),
    P12(id = 12, value = "12"),
    P13(id = 13, value = "13");

    companion object {
        const val MIN_ID = 0
        const val MAX_ID = 13

        fun fromId(id: Int) = entries.find { it.id == id }
    }
}