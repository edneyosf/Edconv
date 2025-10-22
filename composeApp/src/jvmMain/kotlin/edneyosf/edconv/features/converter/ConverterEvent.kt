package edneyosf.edconv.features.converter

import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import edneyosf.edconv.ffmpeg.common.Bitrate
import edneyosf.edconv.ffmpeg.common.Channels
import edneyosf.edconv.ffmpeg.common.Encoder
import edneyosf.edconv.ffmpeg.common.CompressionType
import edneyosf.edconv.ffmpeg.common.PixelFormat
import edneyosf.edconv.ffmpeg.common.Resolution
import edneyosf.edconv.ffmpeg.common.SampleRate

interface ConverterEvent {
    fun setStatus(status: ConverterStatusState) = Unit
    fun setDialog(dialog: ConverterDialogState) = Unit
    fun setOutputFile(fileName: String) = Unit
    fun setOutputDirectory(directory: String) = Unit
    fun setIndexAudio(value: Int?) = Unit
    fun setIndexVideo(value: Int?) = Unit
    fun setEncoderAudio(encoder: Encoder?) = Unit
    fun setEncoderVideo(encoder: Encoder?) = Unit
    fun setCompressionTypeAudio(type: CompressionType?) = Unit
    fun setCompressionTypeVideo(type: CompressionType?) = Unit
    fun setBitrateAudio(bitrate: Bitrate?) = Unit
    fun setBitrateVideo(bitrate: Bitrate?) = Unit
    fun setBitrateControlAudio(value: Int?) = Unit
    fun setBitrateControlVideo(value: Int?) = Unit
    fun setPresetVideo(preset: String?) = Unit
    fun setChannels(channels: Channels?) = Unit
    fun setSampleRate(sampleRate: SampleRate?) = Unit
    fun setResolution(resolution: Resolution?) = Unit
    fun setPixelFormat(pixelFormat: PixelFormat?) = Unit
    fun setNoSubtitle(noSubtitle: Boolean) = Unit
    fun setNoMetadata(noMetadata: Boolean) = Unit
    fun addToQueue(fromStart: Boolean = false, overwrite: Boolean = false) = Unit
    fun start(overwrite: Boolean = false) = Unit
    fun stop() = Unit
    fun setHdr10ToSdr(enabled: Boolean) = Unit
    fun setNoChapters(noChapters: Boolean) = Unit
    fun setTitleVideo(title: String) = Unit
    fun setTitleAudio(title: String) = Unit
}