package core.utils

import core.utils.PropertyUtils.resourcesDir
import java.io.File

object DirUtils {
    val binDir = File(resourcesDir).parentFile.parentFile.parentFile.absolutePath + "/bin/"
    val outputDir = PropertyUtils.userHomeDir + File.separator + "Edconv" + File.separator
}