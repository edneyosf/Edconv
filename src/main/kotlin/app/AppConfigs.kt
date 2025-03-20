package app

import core.aac.AACVBRs
import core.av1.AV1Preset
import core.edconv.common.Channels
import core.edconv.common.Kbps
import core.edconv.common.PixelFormats
import core.edconv.common.Resolutions
import core.h265.H265Preset

object AppConfigs {
    const val TITLE = "Edconv"
    const val CHANNELS_DEFAULT = Channels.STEREO
    const val VBR_DEFAULT = AACVBRs.Q5
    const val KBPS_DEFAULT = Kbps.K192
    const val H265_PRESET_DEFAULT = H265Preset.SLOW
    const val H265_CRF_DEFAULT = 21
    const val AV1_PRESET_DEFAULT = AV1Preset.P4
    const val AV1_CRF_DEFAULT = 25
    const val BIT_DEFAULT = PixelFormats.bit8
    const val NO_AUDIO_DEFAULT = false
    var LANGUAGE = AppLanguages.PT
    val RESOLUTION_DEFAULT = Resolutions.P1080
}