package edneyosf.edconv.features.converter.events

import edneyosf.edconv.ffmpeg.common.*
import edneyosf.edconv.features.converter.states.ConverterDialog
import edneyosf.edconv.features.converter.states.ConverterStatus

interface ConverterEvent {
    fun setStatus(status: ConverterStatus) = Unit
    fun setDialog(dialog: ConverterDialog) = Unit
    fun setCmd(cmd: String) = Unit
    fun setOutput(path: String) = Unit
    fun setCodec(codec: Codec?) = Unit
    fun setCompression(type: CompressionType?) = Unit
    fun setChannels(channels: Channels?) = Unit
    fun setVbr(vbr: Int?) = Unit
    fun setBitrate(bitrate: Bitrate?) = Unit
    fun setSampleRate(sampleRate: SampleRate?) = Unit
    fun setPreset(preset: String?) = Unit
    fun setCrf(crf: Int?) = Unit
    fun setResolution(resolution: Resolution?) = Unit
    fun setPixelFormat(pixelFormat: PixelFormat?) = Unit
    fun setNoAudio(noAudio: Boolean) = Unit
    fun setNoSubtitle(noSubtitle: Boolean) = Unit
    fun setNoMetadata(noMetadata: Boolean) = Unit
    fun start(overwrite: Boolean = false) = Unit
    fun stop() = Unit
}