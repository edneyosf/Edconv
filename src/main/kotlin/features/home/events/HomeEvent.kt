package features.home.events

import edconv.common.*

sealed interface HomeEvent {
    data class SetInputFile(val inputFile: String): HomeEvent
    data class SetOutputFile(val outputFile: String): HomeEvent
    data class SetFormat(val format: MediaFormat?): HomeEvent
    data class SetChannels(val channels: Channels?): HomeEvent
    data class SetVbr(val vbr: Int?): HomeEvent
    data class SetKbps(val kbps: Kbps): HomeEvent
    data class SetSampleRate(val sampleRate: SampleRate?): HomeEvent
    data class SetPreset(val preset: String): HomeEvent
    data class SetCrf(val crf: Int): HomeEvent
    data class SetResolution(val resolution: Resolution?): HomeEvent
    data class SetPixelFormat(val pixelFormat: PixelFormat?): HomeEvent
    data class SetNoAudio(val noAudio: Boolean): HomeEvent
    data object OnStart: HomeEvent
    data object OnStop: HomeEvent
}