package core

import core.utils.PropertyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import java.io.File
import kotlinx.serialization.json.Json

object ConfigManager {

    private const val FILE_NAME = "config.json"
    private lateinit var configFile: File
    private lateinit var config: Config

    fun getFFmpegPath(): String = config.ffmpegPath
    fun getFFprobePath(): String = config.ffprobePath

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

    private suspend fun save(config: Config) = withContext(context = Dispatchers.IO) {
        configFile.writeText(Json.encodeToString(config))
    }

    fun load(appName: String) {
        configFile = File(configDir(appName), FILE_NAME)

        return if (configFile.exists()) config = Json.decodeFromString(configFile.readText())
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