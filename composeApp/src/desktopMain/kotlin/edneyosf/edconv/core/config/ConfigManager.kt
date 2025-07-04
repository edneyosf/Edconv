package edneyosf.edconv.core.config

import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.utils.PropertyUtils
import kotlinx.serialization.json.Json
import java.io.File

open class ConfigManager(private val fileName: String) {

    protected lateinit var configFile: File
    protected lateinit var config: Config

    fun load() {
        configFile = File(configDir(), fileName)

        config = if (configFile.exists()) Json.Default.decodeFromString(string = configFile.readText())
        else Config()
    }

    protected inline fun save(crossinline block: Config.() -> Unit) {
        config.block()
        configFile.writeText(text = Json.Default.encodeToString(value = config))
    }

    private fun configDir(): File {
        val baseDir = when {
            PropertyUtils.isWindows() -> PropertyUtils.userHomeDir
            else -> "${PropertyUtils.userHomeDir}/.config"
        }

        return File(baseDir, AppConfigs.NAME).also { it.mkdirs() }
    }
}