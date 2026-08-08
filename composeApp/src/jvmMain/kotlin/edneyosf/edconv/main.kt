package edneyosf.edconv

import androidx.compose.ui.window.application
import edneyosf.edconv.app.App
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.coreModule
import edneyosf.edconv.feature.console.consoleModule
import edneyosf.edconv.feature.converter.converterModule
import edneyosf.edconv.feature.home.homeFeatureModule
import edneyosf.edconv.feature.mediainfo.mediaInfoModule
import edneyosf.edconv.feature.queue.queueFeatureModule
import edneyosf.edconv.feature.settings.settingsFeatureModule
import edneyosf.edconv.feature.metrics.metricsFeatureModule
import io.github.vinceglb.filekit.FileKit
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    startKoin {
        modules(modules = coreModule)
        modules(modules = homeFeatureModule)
        modules(modules = settingsFeatureModule)
        modules(modules = converterModule)
        modules(modules = queueFeatureModule)
        modules(modules = mediaInfoModule)
        modules(modules = metricsFeatureModule)
        modules(modules = consoleModule)
    }
    System.setProperty("apple.awt.application.appearance", "system")
    FileKit.init(appId = AppConfigs.NAME)
    application { App() }
}