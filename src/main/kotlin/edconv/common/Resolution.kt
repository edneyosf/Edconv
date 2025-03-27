package edconv.common

enum class Resolution(val width: Int, val height: Int, val text: String) {
    P720(width = 1280, height = 720, text = "720p"),
    P1080(width = 1920, height = 1080, text = "1080p"),
    P2160(width = 3840, height = 2160, text = "2160p");

    companion object {
        fun getAll() = entries.toList()
    }
}