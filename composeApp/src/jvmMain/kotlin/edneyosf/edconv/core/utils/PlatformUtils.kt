package edneyosf.edconv.core.utils

import edneyosf.edconv.core.common.OS

object PlatformUtils {
    val current: OS by lazy {
        val osName = System.getProperty("os.name").lowercase()

        when {
            osName.contains(other = "win") -> OS.WINDOWS
            osName.contains(other = "mac") -> OS.MACOS
            else -> OS.LINUX
        }
    }
}