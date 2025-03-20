import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.App
import core.Configs
import core.utils.PropertyUtils
import edconv.aac.AACVBRs
import edconv.av1.AV1Preset
import edconv.common.Channels
import edconv.common.Kbps
import edconv.common.PixelFormats
import edconv.h265.H265Preset
import java.io.File

fun main() {
    setConfigs()

    application {
        Window(
            title = Configs.title,
            onCloseRequest = ::exitApplication,
            content = { App() }
        )
    }
}

private fun setConfigs() {
    Configs.title = "Edconv ${PropertyUtils.version}"
    Configs.noAudioDefault = false
    Configs.channelsDefault = Channels.STEREO
    Configs.vbrDefault = AACVBRs.Q5
    Configs.kbpsDefault = Kbps.K192
    Configs.h265PresetDefault = H265Preset.SLOW
    Configs.av1PresetDefault = AV1Preset.P4
    Configs.h265CrfDefault = 21
    Configs.av1CrfDefault = 25
    Configs.bitDefault = PixelFormats.bit8
    Configs.outputFileDefault = PropertyUtils.userHomeDir + File.separator + "Edconv" + File.separator
}