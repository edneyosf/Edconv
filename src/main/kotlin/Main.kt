import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.App
import core.Configs
import core.utils.DirUtils
import core.utils.PropertyUtils
import edconv.aac.AACVBRs
import edconv.av1.AV1Preset
import edconv.common.Kbps
import edconv.h265.H265Preset
import java.awt.Dimension

fun main() {
    setConfigs()

    application {
        Window(
            title = Configs.title,
            onCloseRequest = ::exitApplication,
            content = {
                window.minimumSize = Dimension(Configs.minWindowWidth, Configs.minWindowHeight)
                App()
            }
        )
    }
}

private fun setConfigs() {
    Configs.title = "Edconv ${PropertyUtils.version}"
    Configs.minWindowWidth = 800
    Configs.minWindowHeight = 600
    Configs.noAudioDefault = false
    Configs.vbrDefault = AACVBRs.Q5
    Configs.aacKbpsDefault = Kbps.K192
    Configs.eac3KbpsDefault = Kbps.K384
    Configs.h265PresetDefault = H265Preset.SLOW.value
    Configs.av1PresetDefault = AV1Preset.P4.value
    Configs.h265CrfDefault = 21
    Configs.av1CrfDefault = 25
    Configs.outputFileDefault = DirUtils.outputDir
}