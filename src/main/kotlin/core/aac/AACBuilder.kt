package core.aac

import core.edconv.EdconvArgs
import core.edconv.args.Formats

data class AACBuilder(
    private val inputFile: String,
    private val outputFile: String,
    private val channels: String? = null,
    private val kbps: String? = null,
    private val vbr: String? = null,
    private val sampleRate: String? = null) {

    fun build(): List<String> {
        val cmd = mutableListOf<String>()

        cmd.add(EdconvArgs.INPUT)
        cmd.add(inputFile)

        cmd.add(EdconvArgs.FORMAT)
        cmd.add(Formats.AAC)

        if(!channels.isNullOrBlank()) {
            cmd.add(EdconvArgs.CHANNELS)
            cmd.add(channels)
        }
        if(!kbps.isNullOrBlank()) {
            cmd.add(EdconvArgs.KBPS)
            cmd.add(kbps)
        }
        if(!vbr.isNullOrBlank()) {
            cmd.add(EdconvArgs.VBR)
            cmd.add(vbr)
        }
        if(!sampleRate.isNullOrBlank()) {
            cmd.add(EdconvArgs.SAMPLE_RATE)
            cmd.add(sampleRate)
        }

        cmd.add(EdconvArgs.OUTPUT)
        cmd.add(outputFile)

        return cmd
    }
}