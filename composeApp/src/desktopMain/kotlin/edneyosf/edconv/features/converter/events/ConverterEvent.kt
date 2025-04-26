package edneyosf.edconv.features.converter.events

import edneyosf.edconv.edconv.common.*
import edneyosf.edconv.edconv.core.data.InputMedia
import edneyosf.edconv.features.converter.states.ConverterDialog
import edneyosf.edconv.features.converter.states.ConverterStatus

sealed interface ConverterEvent {
    data class SetStatus(val status: ConverterStatus): ConverterEvent
    data class SetDialog(val dialog: ConverterDialog): ConverterEvent
    data class SetCmd(val cmd: String): ConverterEvent
    //data class SetInput(val inputMedia: InputMedia): ConverterEvent
    data class SetOutput(val path: String): ConverterEvent
    data class SetCodec(val codec: Codec?): ConverterEvent
    data class SetCompression(val compression: CompressionType?): ConverterEvent
    data class SetChannels(val channels: Channels?): ConverterEvent
    data class SetVbr(val vbr: Int?): ConverterEvent
    data class SetBitrate(val bitrate: Bitrate?): ConverterEvent
    data class SetSampleRate(val sampleRate: SampleRate?): ConverterEvent
    data class SetPreset(val preset: String?): ConverterEvent
    data class SetCrf(val crf: Int?): ConverterEvent
    data class SetResolution(val resolution: Resolution?): ConverterEvent
    data class SetPixelFormat(val pixelFormat: PixelFormat?): ConverterEvent
    data class SetNoAudio(val noAudio: Boolean): ConverterEvent
    data class SetNoSubtitle(val noSubtitle: Boolean): ConverterEvent
    data class SetNoMetadata(val noMetadata: Boolean): ConverterEvent
    data class OnStart(val overwrite: Boolean = false): ConverterEvent
    data object OnStop: ConverterEvent
}