package edneyosf.edconv.feature.home.composables

import androidx.compose.runtime.Composable
import edneyosf.edconv.feature.home.HomeAction
import edneyosf.edconv.feature.home.states.HomeDialogState
import edneyosf.edconv.feature.home.states.HomeState
import edneyosf.edconv.feature.settings.ui.SettingsDialog

@Composable
fun HomeState.Dialogs(event: HomeAction) = dialog.run {
    when(this) {
        is HomeDialogState.Failure -> {
            HomeErrorDialog(
                error = error,
                onFinish = { event.setDialog(HomeDialogState.None) }
            )
        }
        is HomeDialogState.Settings -> SettingsDialog { event.setDialog(HomeDialogState.None) }
        else -> Unit
    }
}