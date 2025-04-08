package edneyosf.edconv

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import edneyosf.edconv.app.App
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.app.AppConfigs.MIN_WINDOW_WIDTH
import edneyosf.edconv.app.AppConfigs.MIN_WINDOW_HEIGHT
import edneyosf.edconv.core.utils.DirUtils
import java.awt.Dimension
import java.awt.Image
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO
import javax.swing.SwingUtilities

fun main() {
    setConfigs()

    application {
        Window(
            title = AppConfigs.NAME,
            onCloseRequest = ::exitApplication,
            content = {
                window.minimumSize = Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
                App()
            }
        )
        setAppIcon("resources/icon.png")
    }
}

fun setAppIcon(path: String) {
    val image: Image? = try {
        val file = File(path)
        println("aaa: "+file.absolutePath)
        ImageIO.read(file)
    } catch (e: Exception) {
        println("error: "+e.message)
        null
    }

    if (image != null) {
        // Espera a janela estar pronta
        println("huhu!")
        SwingUtilities.invokeLater {
            val frames = java.awt.Frame.getFrames()
            frames.forEach { it.iconImage = image }
        }
    }
}

private fun setConfigs() {
    AppConfigs.outputDefault = DirUtils.outputDir
}