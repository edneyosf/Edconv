package edconv.aac

import edconv.common.*
import edconv.core.EdconvArgs

data class AACBuilder(
    override val inputFile: String,
    override val outputFile: String,
    private val channels: Channels? = null,
    private val kbps: Kbps? = null,
    private val vbr: Int? = null,
    private val sampleRate: SampleRate? = null
): MediaBuilder(inputFile, outputFile) {

    init {
        cmd.add(EdconvArgs.FORMAT)
        cmd.add(MediaFormat.AAC.codec)

        if(channels != null) {
            cmd.add(EdconvArgs.CHANNELS)
            cmd.add(channels.value)
        }

        if(kbps != null) {
            cmd.add(EdconvArgs.KBPS)
            cmd.add(kbps.value)
        }

        if(vbr != null) {
            cmd.add(EdconvArgs.VBR)
            cmd.add(vbr.toString())
        }

        if(sampleRate != null) {
            cmd.add(EdconvArgs.SAMPLE_RATE)
            cmd.add(sampleRate.value)
        }
    }
}