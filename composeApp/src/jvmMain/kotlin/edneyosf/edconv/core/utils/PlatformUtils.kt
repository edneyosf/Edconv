package edneyosf.edconv.core.utils

import edneyosf.edconv.core.common.OS
import java.awt.Desktop
import java.net.URI

object PlatformUtils {
    val current: OS by lazy {
        val osName = System.getProperty("os.name").lowercase()

        when {
            osName.contains(other = "win") -> OS.WINDOWS
            osName.contains(other = "mac") -> OS.MACOS
            else -> OS.LINUX
        }
    }

    fun openLink(url: String) : Boolean {
        val supportedAction = Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
        val supported = Desktop.isDesktopSupported() && supportedAction
        var success = true

        try {
            when {
                supported -> Desktop.getDesktop().browse(URI(url))
                current == OS.LINUX -> Runtime.getRuntime().exec(arrayOf("xdg-open", url))
                else -> success = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            success = false
        }

        return success
    }
}