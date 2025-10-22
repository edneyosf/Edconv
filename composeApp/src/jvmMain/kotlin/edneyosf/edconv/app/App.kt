package edneyosf.edconv.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import edconv.composeapp.generated.resources.Res
import edconv.composeapp.generated.resources.icon
import edneyosf.edconv.app.AppConfigs.MIN_WINDOW_HEIGHT
import edneyosf.edconv.app.AppConfigs.MIN_WINDOW_WIDTH
import edneyosf.edconv.app.extensions.getAvailableLanguage
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.features.common.CommonStrings.Keys.CANCEL_BUTTON
import edneyosf.edconv.features.common.CommonStrings.Keys.CONFIRMATION_BUTTON
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.home.ui.HomeScreen
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.languageComp
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.theme.AppTheme
import edneyosf.edconv.app.ClosingDialogStrings.Keys.TITLE_CLOSING_DIALOG
import edneyosf.edconv.app.ClosingDialogStrings.Keys.DESCRIPTION_CLOSING_DIALOG
import edneyosf.edconv.ui.compositions.fileKitDialogSettingsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import java.awt.Dimension

@Composable
fun ApplicationScope.App() {
    val process: EdProcess = koinInject()
    val language = Locale.current.getAvailableLanguage()
    var showClosingDialog by mutableStateOf(value = false)

    Window(
        title = AppConfigs.NAME,
        onCloseRequest = {
            if(!process.isBusy()) exitApplication()
            else showClosingDialog = true
        },
        icon = painterResource(resource = Res.drawable.icon)
    ) {
        window.minimumSize = Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT)
        CompositionLocalProvider(
            languageComp provides language,
            fileKitDialogSettingsComp provides FileKitDialogSettings(parentWindow = window)
        ) {
            AppTheme {
                ClosingDialog(
                    show = showClosingDialog,
                    onDismiss = { showClosingDialog = false },
                    onExit = {
                        process.killAll()
                        exitApplication()
                    }
                )
                HomeScreen()
            }
        }
    }
}

@Composable
private fun ClosingDialog(show: Boolean, onExit: () -> Unit, onDismiss: () -> Unit) {
    if(show) {
        CompositionLocalProvider(value = stringsComp provides closingDialogStrings) {
            SimpleDialog(
                icon = Icons.Rounded.Warning,
                title = strings[TITLE_CLOSING_DIALOG],
                cancelText = commonStrings[CANCEL_BUTTON],
                confirmationText = commonStrings[CONFIRMATION_BUTTON],
                onCancel = onDismiss,
                onConfirmation = onExit,
                onDismissRequest = onDismiss,
                content = { Text(text = strings[DESCRIPTION_CLOSING_DIALOG]) }
            )
        }
    }
}

@Composable
private fun DefaultPreview() {
    ClosingDialog(show = true, onExit = {}, onDismiss = {})
}

@Preview
@Composable
private fun EnglishLight() = EnglishLightPreview { DefaultPreview() }

@Preview
@Composable
private fun EnglishDark() = EnglishDarkPreview { DefaultPreview() }

@Preview
@Composable
private fun PortugueseLight() = PortugueseLightPreview { DefaultPreview() }

@Preview
@Composable
private fun PortugueseDark() = PortugueseDarkPreview { DefaultPreview() }