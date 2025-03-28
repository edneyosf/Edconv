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
    val version = PropertyUtils.version

    Configs.title = "Edconv ${if(version != null) "v$version" else "- Dev"}"
    Configs.minWindowWidth = 1250
    Configs.minWindowHeight = 730
    Configs.outputFileDefault = DirUtils.outputDir
}