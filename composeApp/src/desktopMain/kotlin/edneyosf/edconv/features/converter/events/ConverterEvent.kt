package edneyosf.edconv.features.converter.events

import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.common.*
import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterStatusState

interface ConverterEvent {
    fun refresh(newInput: InputMedia, newType: MediaType) = Unit
    fun setStatus(status: ConverterStatusState) = Unit
    fun setDialog(dialog: ConverterDialogState) = Unit
    fun setCommand(cmd: String) = Unit
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
    fun addToQueue(overwrite: Boolean = false) = Unit
    fun start(overwrite: Boolean = false) = Unit
    fun stop() = Unit
}