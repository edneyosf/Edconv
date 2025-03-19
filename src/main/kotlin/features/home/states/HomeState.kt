package features.home.states

import core.edconv.common.Resolutions
import features.home.states.status.HomeStatus

data class HomeState(
    val status: HomeStatus,
    var inputFile: String?,
    var outputFile: String,
    var format: String,
    var channels: String,
    var vbr: String,
    var kbps: String,
    var sampleRate: String?,
    var preset: String?,
    var crf: Int,
    var resolution: Resolutions,
    var bit: String,
    var noAudio: Boolean,
)