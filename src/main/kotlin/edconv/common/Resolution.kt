package edconv.common

import kotlin.math.absoluteValue

enum class Resolution(val width: Int, val height: Int, val text: String) {
    P240(width = 426, height = 240, text = "240p"),
    P360(width = 640, height = 360, text = "360p"),
    P480(width = 854, height = 480, text = "480p"),
    P720(width = 1280, height = 720, text = "720p"),
    P1080(width = 1920, height = 1080, text = "1080p"),
    P2160(width = 3840, height = 2160, text = "2160p");

    fun preserveAspectRatioFilter(sourceWidth: Int, sourceHeight: Int) = when {
        sourceWidth == width -> "scale=$width:-1"
        sourceHeight == height -> "scale=-1:$height"
        (sourceWidth - width).absoluteValue > (sourceHeight - height).absoluteValue -> "scale=-1:$height"
        else -> "scale=$width:-1"
    }

    companion object {
        fun getAll() = entries.toList()
    }
}