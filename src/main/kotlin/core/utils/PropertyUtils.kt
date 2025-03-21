package core.utils

object PropertyUtils {
    val resourcesDir = System.getProperty("compose.application.resources.dir") ?: ""
    val userHomeDir = System.getProperty("user.home") ?: ""
    val version = System.getProperty("jpackage.app-version") ?: "version"
}