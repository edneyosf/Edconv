package edneyosf.edconv

import androidx.compose.ui.window.application
import edneyosf.edconv.app.App
import edneyosf.edconv.core.coreModule
import edneyosf.edconv.features.converter.converterHomeModule
import edneyosf.edconv.features.home.homeFeatureModule
import edneyosf.edconv.features.queue.queueFeatureModule
import edneyosf.edconv.features.settings.settingsFeatureModule
import edneyosf.edconv.features.vmaf.vmafFeatureModule
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    startKoin {
        modules(modules = coreModule)
        modules(modules = homeFeatureModule)
        modules(modules = settingsFeatureModule)
        modules(modules = converterHomeModule)
        modules(modules = queueFeatureModule)
        modules(modules = vmafFeatureModule)
    }
    application { App() }
}