import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.App
import app.AppConfigs

fun main() = application {
    Window(
        title = AppConfigs.TITLE,
        onCloseRequest = ::exitApplication) {
        App()
    }
}