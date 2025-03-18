package core.eac3

import core.common.MediaBuilder
import core.edconv.EdconvArgs
import core.edconv.common.Formats

data class EAC3Builder(
    override val inputFile: String,
    override val outputFile: String,
    private val channels: String? = null,
    private val kbps: String? = null,
    private val sampleRate: String? = null
): MediaBuilder(inputFile, outputFile) {

    init {
        cmd.add(EdconvArgs.FORMAT)
        cmd.add(Formats.EAC3)

        if(!channels.isNullOrBlank()) {
            cmd.add(EdconvArgs.CHANNELS)
            cmd.add(channels)
        }

        if(!kbps.isNullOrBlank()) {
            cmd.add(EdconvArgs.KBPS)
            cmd.add(kbps)
        }

        if(!sampleRate.isNullOrBlank()) {
            cmd.add(EdconvArgs.SAMPLE_RATE)
            cmd.add(sampleRate)
        }
    }
}