package edneyosf.edconv.edconv.common

enum class MediaType {
    AUDIO, VIDEO;

    companion object {
        fun fromIndex(index: Int) = entries.find { it.ordinal == index }
    }
}