package edneyosf.edconv.features.converter

import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import edneyosf.edconv.ffmpeg.common.Bitrate
import edneyosf.edconv.ffmpeg.common.Channels
import edneyosf.edconv.ffmpeg.common.Codec
import edneyosf.edconv.ffmpeg.common.CompressionType
import edneyosf.edconv.ffmpeg.common.PixelFormat
import edneyosf.edconv.ffmpeg.common.Resolution
import edneyosf.edconv.ffmpeg.common.SampleRate

interface ConverterEvent {
    fun setStatus(status: ConverterStatusState) = Unit
    fun setDialog(dialog: ConverterDialogState) = Unit
    fun setOutput(fileName: String) = Unit
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
    fun addToQueue(fromStart: Boolean = false, overwrite: Boolean = false) = Unit
    fun start(overwrite: Boolean = false) = Unit
    fun stop() = Unit
    fun pickFolder(title: String, fileName: String) = Unit
    fun setHdr10ToSdr(enabled: Boolean) = Unit
}