package edneyosf.edconv.core.config

import edneyosf.edconv.core.utils.PropertyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

object ConfigManager {

    private const val FILE_NAME = "config.json"
    private lateinit var configFile: File
    private lateinit var config: Config

    fun getFFmpegPath(): String = config.ffmpegPath
    fun getFFprobePath(): String = config.ffprobePath

    fun getVmafModelPath(): String = config.vmafModelPath

    suspend fun setFFmpegPath(path: String) {
        val newConfig = config.copy(ffmpegPath = path)
        save(newConfig)
        config = newConfig
    }

    suspend fun setFFprobePath(path: String) {
        val newConfig = config.copy(ffprobePath = path)
        save(newConfig)
        config = newConfig
    }

    suspend fun setVmafModelPath(path: String) {
        val newConfig = config.copy(vmafModelPath = path)
        save(newConfig)
        config = newConfig
    }

    private suspend fun save(config: Config) = withContext(context = Dispatchers.IO) {
        configFile.writeText(Json.Default.encodeToString(config))
    }

    fun load(appName: String) {
        configFile = File(configDir(appName), FILE_NAME)

        return if (configFile.exists()) config = Json.Default.decodeFromString(configFile.readText())
        else config = Config()
    }

    private fun configDir(appName: String): File {
        val baseDir = when {
            isWindows() -> PropertyUtils.userHomeDir
            else -> "${PropertyUtils.userHomeDir}/.config"
        }

        return File(baseDir, appName).also { it.mkdirs() }
    }

    private fun isWindows(): Boolean = PropertyUtils.osName.contains("win")
}