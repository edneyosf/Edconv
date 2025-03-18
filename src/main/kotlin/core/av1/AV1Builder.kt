package core.av1

import core.edconv.EdconvArgs
import core.edconv.args.Formats
import core.edconv.args.Resolutions

data class AV1Builder(
    private val inputFile: String,
    private val outputFile: String,
    private val bit: String? = null,
    private val crf: String,
    private val noAudio: Boolean = false,
    private val preset: String,
    private val resolution: Resolutions
) {
    fun build(): List<String> {
        val cmd = mutableListOf<String>()

        cmd.add(EdconvArgs.INPUT)
        cmd.add(inputFile)

        cmd.add(EdconvArgs.FORMAT)
        cmd.add(Formats.AV1)

        cmd.add(EdconvArgs.CRF)
        cmd.add(crf)

        cmd.add(EdconvArgs.PRESET)
        cmd.add(preset)

        cmd.add(EdconvArgs.WIDTH)
        cmd.add(resolution.width.toString())

        if(!bit.isNullOrBlank()) {
            cmd.add(EdconvArgs.BIT)
            cmd.add(bit)
        }
        if(noAudio) cmd.add(EdconvArgs.NO_AUDIO)

        cmd.add(EdconvArgs.OUTPUT)
        cmd.add(outputFile)

        return cmd
    }
}