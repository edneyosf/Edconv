package edneyosf.edconv

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import edconv.composeapp.generated.resources.Res
import edconv.composeapp.generated.resources.icon
import edneyosf.edconv.app.App
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.app.AppConfigs.MIN_WINDOW_WIDTH
import edneyosf.edconv.app.AppConfigs.MIN_WINDOW_HEIGHT
import edneyosf.edconv.core.utils.DirUtils
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

fun main() {
    setConfigs()

    application {
        Window(
            title = AppConfigs.NAME,
            onCloseRequest = ::exitApplication,
            icon = painterResource(Res.drawable.icon),
            content = {
                window.minimumSize = Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
                App()
            }
        )
    }
}

private fun setConfigs() {
    AppConfigs.outputDefault = DirUtils.outputDir
}