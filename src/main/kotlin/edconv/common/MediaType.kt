package edconv.common

enum class MediaType(val index: Int) {
    AUDIO(index = 0),
    VIDEO(index = 1);

    companion object {
        fun fromIndex(index: Int) = entries.find { it.index == index }
    }
}