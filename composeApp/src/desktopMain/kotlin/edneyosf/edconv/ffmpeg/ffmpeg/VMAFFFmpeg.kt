package edneyosf.edconv.ffmpeg.ffmpeg

import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs.FORCE
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs.INPUT
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs.OVERRIDE_INPUT
import java.io.File

class VMAFFFmpeg(
    val input: File,
    val source: File,
    val fps: Int,
    val model: File,
    val width: Int,
    val height: Int,
    val threads: Int
) {
    fun command(): Array<String> {
        val data = mutableListOf<String>()

        //Source file
        data.add(OVERRIDE_INPUT)
        data.add(fps.toString())
        data.add(INPUT)
        data.add(source.absolutePath)

        //Input file
        data.add(OVERRIDE_INPUT)
        data.add(fps.toString())
        data.add(INPUT)
        data.add(input.absolutePath)

        data.add("-lavfi")
        data.add("\"[0:v]setpts=PTS-STARTPTS[reference];[1:v]scale=$width:$height:flags=bicubic,setpts=PTS-STARTPTS[distorted];[distorted][reference]libvmaf=log_fmt=xml:log_path=${model.absolutePath}:n_threads=${threads}\"")

        data.add(FORCE)
        data.add("null")
        data.add("-")

        return data.toTypedArray()
    }
}
