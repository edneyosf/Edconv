package core.utils

import java.io.File

object PropertyUtils {
    val resourcesDir = System.getProperty("compose.application.resources.dir") ?: ""
    val userHomeDir = System.getProperty("user.home") ?: ""
    val binDir = File(resourcesDir).parentFile.parentFile.parentFile.absolutePath + "/bin/"
    val version = System.getProperty("jpackage.app-version") ?: "version"
}