package edneyosf.edconv.features.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.events.HomeEvent.OnStart
import edneyosf.edconv.features.home.events.HomeEvent.SetStatus
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.states.HomeStatus
import edneyosf.edconv.features.home.strings.homeDialogStrings
import edneyosf.edconv.features.home.strings.HomeDialogStrings.Keys.*
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Keys.DEFAULT_ERROR
import edneyosf.edconv.features.settings.SettingsDialog
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun HomeState.Dialogs(onEvent: (HomeEvent) -> Unit) {
    when (this.status) {
        is HomeStatus.Settings -> SettingsDialog { onEvent(SetStatus(HomeStatus.Initial)) }

        is HomeStatus.Error -> {
            ErrorDialog(
                message = status.message ?: strings[DEFAULT_ERROR],
                onFinish = { onEvent(SetStatus(HomeStatus.Initial)) }
            )
        }

        is HomeStatus.Complete -> {
            CompleteDialog(
                startTime = status.startTime,
                finishTime = status.finishTime,
                duration = status.duration,
                onFinish = { onEvent(SetStatus(HomeStatus.Initial)) }
            )
        }

        is HomeStatus.FileExists -> {
            OverwriteFileDialog(
                onCancel = { onEvent(SetStatus(HomeStatus.Initial)) },
                onConfirmation = { onEvent(OnStart(overwrite = true)) }
            )
        }

        else -> Unit
    }
}

@Composable
fun ErrorDialog(message: String, onFinish: () -> Unit) {
    CompositionLocalProvider(stringsComp provides homeDialogStrings) {
        SimpleDialog(
            icon = Icons.Rounded.Error,
            title = strings[ERROR_TITLE],
            content = { Text(message) },
            onConfirmation = onFinish,
            confirmationText = strings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
fun OverwriteFileDialog(onCancel: () -> Unit, onConfirmation: () -> Unit) {
    CompositionLocalProvider(stringsComp provides homeDialogStrings) {
        SimpleDialog(
            icon = Icons.Rounded.Warning,
            title = strings[WARNING_TITLE],
            content = { Text(strings[OVERWRITE_FILE]) },
            confirmationText = strings[CONFIRMATION_BUTTON],
            onConfirmation = onConfirmation,
            cancelText = strings[CANCEL_BUTTON],
            onCancel = onCancel
        )
    }
}

@Composable
fun CompleteDialog(startTime: String, finishTime: String, duration: String, onFinish: () -> Unit) {
    CompositionLocalProvider(stringsComp provides homeDialogStrings) {
        SimpleDialog(
            title = strings[COMPLETE_TITLE],
            content = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(strings[START_TIME])
                        Spacer(modifier = Modifier.width(dimens.xxs))
                        Text(startTime, style = MaterialTheme.typography.labelLarge)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(strings[END_TIME])
                        Spacer(modifier = Modifier.width(dimens.xxs))
                        Text(finishTime, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(dimens.xs))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val color = MaterialTheme.colorScheme.tertiary

                        Text(strings[DURATION_TIME], style = MaterialTheme.typography.bodyLarge.copy(color = color))
                        Spacer(modifier = Modifier.width(dimens.xxs))
                        Text(duration, style = MaterialTheme.typography.titleMedium.copy(color = color))
                    }
                }
            },
            icon = Icons.Rounded.CheckCircle,
            onConfirmation = onFinish,
            confirmationText = strings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
private fun ErrorPreview() = ErrorDialog("Sample", onFinish = {})

@Composable
private fun OverwriteFilePreview() = OverwriteFileDialog(onConfirmation = {}, onCancel = {})

@Composable
private fun CompletePreview() = CompleteDialog(startTime = "123", finishTime = "123", duration = "123", onFinish = {})

@Preview
@Composable
private fun ErrorEnglishLight() = EnglishLightPreview { ErrorPreview() }

@Preview
@Composable
private fun ErrorEnglishDark() = EnglishDarkPreview { ErrorPreview() }

@Preview
@Composable
private fun ErrorPortugueseLight() = PortugueseLightPreview { ErrorPreview() }

@Preview
@Composable
private fun ErrorPortugueseDark() = PortugueseDarkPreview { ErrorPreview() }

@Preview
@Composable
private fun OverwriteFileEnglishLight() = EnglishLightPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFileEnglishDark() = EnglishDarkPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFilePortugueseLight() = PortugueseLightPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFilePortugueseDark() = PortugueseDarkPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun CompleteEnglishLight() = EnglishLightPreview { CompletePreview() }

@Preview
@Composable
private fun CompleteEnglishDark() = EnglishDarkPreview { CompletePreview() }

@Preview
@Composable
private fun CompletePortugueseLight() = PortugueseLightPreview { CompletePreview() }

@Preview
@Composable
private fun CompletePortugueseDark() = PortugueseDarkPreview { CompletePreview() }