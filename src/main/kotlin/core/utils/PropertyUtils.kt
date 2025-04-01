package core.utils

object PropertyUtils {
    val resourcesDir = System.getProperty("compose.application.resources.dir") ?: ""
    val userHomeDir = System.getProperty("user.home") ?: ""
    val version: String? = System.getProperty("jpackage.app-version")
}