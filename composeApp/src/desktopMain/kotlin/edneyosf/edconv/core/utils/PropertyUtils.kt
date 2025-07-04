package edneyosf.edconv.core.utils

object PropertyUtils {

    val userHomeDir = System.getProperty("user.home") ?: ""
    val version: String? = System.getProperty("jpackage.app-version")
    val osName: String = System.getProperty("os.name").lowercase()

    fun isWindows(): Boolean = osName.contains(other = "win")
}