package edneyosf.edconv

import androidx.compose.ui.window.application
import edneyosf.edconv.app.App
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.coreModule
import edneyosf.edconv.features.console.consoleModule
import edneyosf.edconv.features.converter.converterModule
import edneyosf.edconv.features.home.homeFeatureModule
import edneyosf.edconv.features.queue.queueFeatureModule
import edneyosf.edconv.features.settings.settingsFeatureModule
import edneyosf.edconv.features.metrics.metricsFeatureModule
import io.github.vinceglb.filekit.FileKit
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    startKoin {
        modules(modules = coreModule)
        modules(modules = homeFeatureModule)
        modules(modules = settingsFeatureModule)
        modules(modules = converterModule)
        modules(modules = queueFeatureModule)
        modules(modules = metricsFeatureModule)
        modules(modules = consoleModule)
    }
    FileKit.init(appId = AppConfigs.NAME)
    application { App() }
}