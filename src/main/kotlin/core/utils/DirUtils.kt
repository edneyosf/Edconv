package core.utils

import java.io.File

object DirUtils {
    val resources = System.getProperty("compose.application.resources.dir") ?: ""
    val userHome = System.getProperty("user.home") ?: ""
    val bin = File(resources).parentFile.parentFile.parentFile.absolutePath + "/bin/"
}