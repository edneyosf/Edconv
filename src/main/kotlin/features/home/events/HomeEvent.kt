package features.home.events

import edconv.common.Resolutions

sealed interface HomeEvent {
    data class SetInputFile(val inputFile: String): HomeEvent
    data class SetOutputFile(val outputFile: String): HomeEvent
    data class SetFormat(val format: String): HomeEvent
    data class SetChannels(val channels: String): HomeEvent
    data class SetVbr(val vbr: String): HomeEvent
    data class SetKbps(val kbps: String): HomeEvent
    data class SetSampleRate(val sampleRate: String?): HomeEvent
    data class SetPreset(val preset: String?): HomeEvent
    data class SetCrf(val crf: Int): HomeEvent
    data class SetResolution(val resolution: Resolutions): HomeEvent
    data class SetBit(val bit: String): HomeEvent
    data class SetNoAudio(val noAudio: Boolean): HomeEvent
    data object OnStart: HomeEvent
}