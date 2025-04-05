import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.App
import app.AppConfigs
import app.AppConfigs.MIN_WINDOW_WIDTH
import app.AppConfigs.MIN_WINDOW_HEIGHT
import core.utils.DirUtils
import core.utils.PropertyUtils
import java.awt.Dimension

fun main() {
    setConfigs()

    application {
        Window(
            title = AppConfigs.title,
            onCloseRequest = ::exitApplication,
            content = {
                window.minimumSize = Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
                App()
            }
        )
    }
}

private fun setConfigs() {
    val version = PropertyUtils.version

    AppConfigs.title = "${AppConfigs.NAME} ${if(version != null) "v$version" else "- Dev"}"
    AppConfigs.outputDefault = DirUtils.outputDir
}