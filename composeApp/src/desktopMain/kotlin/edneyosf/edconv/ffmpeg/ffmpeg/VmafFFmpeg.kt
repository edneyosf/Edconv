package edneyosf.edconv.ffmpeg.ffmpeg

import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs.FILTER_AV
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs.FORCE
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs.INPUT
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs.OVERRIDE_INPUT
import edneyosf.edconv.ffmpeg.ffmpeg.extensions.addCmd

class VmafFFmpeg(
    val reference: String,
    val distorted: String,
    val referenceDim: Pair<Int, Int>,
    val distortedDim: Pair<Int, Int>,
    val fps: Int,
    val model: String,
    val threads: Int
) {
    fun command(): Array<String> {
        val data = mutableListOf<String>()

        data.addCmd(param = OVERRIDE_INPUT, value = fps.toString())
        data.addCmd(param = INPUT, value = reference)
        data.addCmd(param = OVERRIDE_INPUT, value = fps.toString())
        data.addCmd(param = INPUT, value = distorted)
        data.addCmd(param = FILTER_AV, value = filter())

        data.add(FORCE)
        data.add("null")
        data.add("-")

        return data.toTypedArray()
    }

    private fun filter(): String {
        val builder = StringBuilder()
        val referenceWidth = referenceDim.first
        val referenceHeight = referenceDim.second
        val distortedWidth = distortedDim.first
        val distortedHeight = distortedDim.second
        val sameWidth = referenceWidth == distortedWidth
        val sameHeight = referenceHeight == distortedHeight
        val sameDim = sameWidth && sameHeight
        var referenceScale = ""
        var distortedScale = ""

        if(!sameDim) {
            if(referenceWidth > distortedWidth || referenceHeight > distortedHeight) {
                referenceScale = scale(distortedDim)
                distortedScale = ""
            }
            else {
                referenceScale = ""
                distortedScale = scale(referenceDim)
            }
        }

        builder.append("[0:v]${referenceScale}setpts=PTS-STARTPTS[reference];")
        builder.append("[1:v]${distortedScale}setpts=PTS-STARTPTS[distorted];")
        builder.append("[distorted][reference]libvmaf=log_fmt=xml:")
        builder.append("log_path=$model:")
        builder.append("n_threads=$threads")

        return builder.toString()
    }

    private fun scale(dimens: Pair<Int, Int>) = "scale=${dimens.first}:${dimens.second}:flags=bicubic,"
}