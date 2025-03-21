package edconv.core.utils

object CmdUtils {
    fun killProcess(processName: String): Boolean {
        var isSuccess = false

        try {
            val cmd = arrayOf("pkill", processName)
            val process = Runtime.getRuntime().exec(cmd)

            process.waitFor()
            isSuccess = process.exitValue() == 0
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return isSuccess
    }
}