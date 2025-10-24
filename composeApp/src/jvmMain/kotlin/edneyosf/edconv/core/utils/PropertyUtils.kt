package edneyosf.edconv.core.utils

object PropertyUtils {
    val userHomeDir = System.getProperty("user.home") ?: ""
    val version: String? = System.getProperty("jpackage.app-version")
}