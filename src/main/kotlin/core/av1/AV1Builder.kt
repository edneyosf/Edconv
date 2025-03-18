package core.av1

import core.common.MediaBuilder
import core.edconv.EdconvArgs
import core.edconv.common.MediaFormat
import core.edconv.common.Resolutions

data class AV1Builder(
    override val inputFile: String,
    override val outputFile: String,
    private val bit: String? = null,
    private val crf: Int,
    private val noAudio: Boolean = false,
    private val preset: String,
    private val resolution: Resolutions
): MediaBuilder(inputFile, outputFile) {

    init {
        cmd.add(EdconvArgs.FORMAT)
        cmd.add(MediaFormat.AV1.codec)

        cmd.add(EdconvArgs.CRF)
        cmd.add(crf.toString())

        cmd.add(EdconvArgs.PRESET)
        cmd.add(preset)

        cmd.add(EdconvArgs.WIDTH)
        cmd.add(resolution.width.toString())

        if(!bit.isNullOrBlank()) {
            cmd.add(EdconvArgs.BIT)
            cmd.add(bit)
        }

        if(noAudio) cmd.add(EdconvArgs.NO_AUDIO)
    }
}