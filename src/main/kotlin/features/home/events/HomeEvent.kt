package features.home.events

import edconv.common.*
import features.home.states.HomeStatus

sealed interface HomeEvent {
    data class SetStatus(val status: HomeStatus): HomeEvent
    data class SetInputFile(val inputFile: String): HomeEvent
    data class SetOutputFile(val outputFile: String): HomeEvent
    data class SetCodec(val codec: Codec?): HomeEvent
    data class SetChannels(val channels: Channels?): HomeEvent
    data class SetVbr(val vbr: Int?): HomeEvent
    data class SetBitrate(val bitrate: Bitrate): HomeEvent
    data class SetSampleRate(val sampleRate: SampleRate?): HomeEvent
    data class SetPreset(val preset: String): HomeEvent
    data class SetCrf(val crf: Int): HomeEvent
    data class SetResolution(val resolution: Resolution?): HomeEvent
    data class SetPixelFormat(val pixelFormat: PixelFormat?): HomeEvent
    data class SetNoAudio(val noAudio: Boolean): HomeEvent
    data class OnStart(val overwrite: Boolean = false): HomeEvent
    data object OnStop: HomeEvent
}