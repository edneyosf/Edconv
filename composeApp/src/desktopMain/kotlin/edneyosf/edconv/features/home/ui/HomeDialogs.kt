package edneyosf.edconv.features.home.ui

import androidx.compose.runtime.Composable
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.states.HomeDialogState
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.settings.ui.SettingsDialog

@Composable
fun HomeState.Dialogs(event: HomeEvent) = dialog.run {
    when(this) {
        is HomeDialogState.Error -> {
            HomeErrorDialog(
                error = id,
                onFinish = { event.setDialog(HomeDialogState.None) }
            )
        }
        is HomeDialogState.Settings -> SettingsDialog { event.setDialog(HomeDialogState.None) }
        else -> Unit
    }
}