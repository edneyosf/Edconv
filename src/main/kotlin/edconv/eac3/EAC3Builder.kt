package edconv.eac3

import edconv.common.*
import edconv.core.EdconvArgs

data class EAC3Builder(
    override val inputFile: String,
    override val outputFile: String,
    private val channels: Channels? = null,
    private val kbps: Kbps,
    private val sampleRate: SampleRate? = null
): MediaBuilder(inputFile, outputFile) {

    init {
        cmd.add(EdconvArgs.FORMAT)
        cmd.add(MediaFormat.EAC3.codec)

        if(channels != null) {
            cmd.add(EdconvArgs.CHANNELS)
            cmd.add(channels.value)
        }

        cmd.add(EdconvArgs.KBPS)
        cmd.add(kbps.value)

        if(sampleRate != null) {
            cmd.add(EdconvArgs.SAMPLE_RATE)
            cmd.add(sampleRate.value)
        }
    }
}