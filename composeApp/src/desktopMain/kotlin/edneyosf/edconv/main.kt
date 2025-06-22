package edneyosf.edconv

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import edconv.composeapp.generated.resources.Res
import edconv.composeapp.generated.resources.icon
import edneyosf.edconv.app.App
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.app.AppConfigs.MIN_WINDOW_WIDTH
import edneyosf.edconv.app.AppConfigs.MIN_WINDOW_HEIGHT
import edneyosf.edconv.core.coreModule
import edneyosf.edconv.features.converter.converterHomeModule
import edneyosf.edconv.features.home.homeFeatureModule
import edneyosf.edconv.features.queue.queueFeatureModule
import edneyosf.edconv.features.settings.settingsFeatureModule
import edneyosf.edconv.features.vmaf.vmafFeatureModule
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.GlobalContext.startKoin
import java.awt.Dimension

fun main() {
    setModules()
    application {
        Window(
            title = AppConfigs.NAME,
            onCloseRequest = ::exitApplication,
            icon = painterResource(resource = Res.drawable.icon),
            content = {
                window.minimumSize = Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
                App()
            }
        )
    }
}

private fun setModules() {
    startKoin {
        modules(modules = coreModule)
        modules(modules = homeFeatureModule)
        modules(modules = settingsFeatureModule)
        modules(modules = converterHomeModule)
        modules(modules = queueFeatureModule)
        modules(modules = vmafFeatureModule)
    }
}