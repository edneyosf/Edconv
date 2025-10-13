package edneyosf.edconv.ffmpeg.common

import kotlin.math.absoluteValue

enum class Resolution(val width: Int, val height: Int, val text: String) {
    P480(width = 854, height = 480, text = "SD (480p)"),
    P720(width = 1280, height = 720, text = "HD (720p)"),
    P1080(width = 1920, height = 1080, text = "Full HD (1080p)"),
    P1440(width = 2560, height = 1440, text = "QHD (1440p)"),
    P2160(width = 3840, height = 2160, text = "4K UHD (2160p)"),
    P4320(width = 7680, height = 4320, text = "8K UHD (4320p)");

    private val toneMap = "hable"
    private val scalingFilter = "spline36"

    fun preserveAspectRatioFilter(sourceWidth: Int, sourceHeight: Int, hdr10ToSdr: Boolean): String? {
        val hdr10ToSdrFilter = if(hdr10ToSdr)
            "zscale=t=linear:npl=100,format=gbrpf32le,tonemap=$toneMap:desat=0,zscale=t=bt709:m=bt709:p=bt709:r=tv"
        else ""

        val scaleFilter = when {
            sourceWidth == width || sourceHeight == height -> ""
            (sourceWidth - width).absoluteValue > (sourceHeight - height).absoluteValue -> "zscale=-1:$height:f=$scalingFilter"
            else -> "zscale=$width:-1:f=$scalingFilter"
        }

        return listOf(hdr10ToSdrFilter, scaleFilter)
            .filter { it.isNotBlank() }
            .takeIf { it.isNotEmpty() }
            ?.joinToString(separator = ",")
    }

    companion object {
        fun getAll() = entries.toList()

        fun fromValues(width: Int?, height: Int?): Resolution? {
            val resolutions = Resolution.entries

            return when {
                width != null -> resolutions.firstOrNull { it.width == width }
                height != null -> resolutions.firstOrNull { it.height == height }
                else -> null
            }
        }
    }
}