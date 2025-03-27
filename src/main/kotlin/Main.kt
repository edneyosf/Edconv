import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.App
import core.Configs
import core.utils.DirUtils
import core.utils.PropertyUtils
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
    Configs.outputFileDefault = DirUtils.outputDir
}