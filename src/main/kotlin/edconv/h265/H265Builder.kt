package edconv.h265

import edconv.common.MediaBuilder
import edconv.common.MediaFormat
import edconv.common.Resolutions
import edconv.core.EdconvArgs

data class H265Builder(
    override val inputFile: String,
    override val outputFile: String,
    private val bit: String? = null,
    private val crf: Int,
    private val noAudio: Boolean = false,
    private val preset: String,
    private val resolution: Resolutions?
): MediaBuilder(inputFile, outputFile) {

    init {
        cmd.add(EdconvArgs.FORMAT)
        cmd.add(MediaFormat.H265.codec)

        cmd.add(EdconvArgs.CRF)
        cmd.add(crf.toString())

        cmd.add(EdconvArgs.PRESET)
        cmd.add(preset)

        if(resolution != null) {
            cmd.add(EdconvArgs.WIDTH)
            cmd.add(resolution.width.toString())
        }

        if(!bit.isNullOrBlank()) {
            cmd.add(EdconvArgs.BIT)
            cmd.add(bit)
        }

        if(noAudio) cmd.add(EdconvArgs.NO_AUDIO)
    }
}