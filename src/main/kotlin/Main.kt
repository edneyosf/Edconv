import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.App
import app.Configs

fun main() = application {
    Window(
        title = Configs.TITLE,
        onCloseRequest = ::exitApplication) {
        App()
    }
}