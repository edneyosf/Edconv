package edconv.aac

import edconv.common.Channels
import edconv.common.MediaBuilder
import edconv.common.MediaFormat
import edconv.core.EdconvArgs

data class AACBuilder(
    override val inputFile: String,
    override val outputFile: String,
    private val channels: Channels? = null,
    private val kbps: String? = null,
    private val vbr: String? = null,
    private val sampleRate: String? = null
): MediaBuilder(inputFile, outputFile) {

    init {
        cmd.add(EdconvArgs.FORMAT)
        cmd.add(MediaFormat.AAC.codec)

        if(channels != null) {
            cmd.add(EdconvArgs.CHANNELS)
            cmd.add(channels.value)
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
    }
}