package edneyosf.edconv.features.vmaf.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import edneyosf.edconv.core.extensions.LaunchedEffected
import edneyosf.edconv.features.converter.strings.ConverterScreenStrings.Keys.MEDIA_INFO
import edneyosf.edconv.features.vmaf.strings.VMAFScreenStrings.Keys.*
import edneyosf.edconv.features.vmaf.VmafArgs
import edneyosf.edconv.features.vmaf.events.VmafEvent
import edneyosf.edconv.features.vmaf.states.VmafDialogState
import edneyosf.edconv.features.vmaf.states.VmafState
import edneyosf.edconv.features.vmaf.states.VmafStatusState
import edneyosf.edconv.features.vmaf.strings.vmafScreenStrings
import edneyosf.edconv.features.vmaf.viewmodels.VmafViewModel
import edneyosf.edconv.ui.components.ActionsTool
import edneyosf.edconv.ui.components.TextTooltip
import edneyosf.edconv.ui.components.buttons.PrimaryButton
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp

@Composable
fun VMAFScreen(args: VmafArgs) {
    val viewModel = viewModel { VmafViewModel(input = args.input) }
    val state by viewModel.state

    LaunchedEffected(key = args) { viewModel.refresh(newInput = it.input) }

    CompositionLocalProvider(value = stringsComp provides vmafScreenStrings) {
        state.Dialogs(event = viewModel)
        state.Content(
            event = viewModel
        )
    }
}

@Composable
private fun VmafState.Content(event: VmafEvent) {
    Column(
        modifier = Modifier.padding(all = dimens.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = dimens.xl)
    ) {
        ActionsTool(
            startEnabled = canStart(),
            stopEnabled = canStop(),
            startDescription = strings[START_CONVERSION],
            stopDescription = strings[STOP_CONVERSION],
            onStart = { event.start() },
            onStop = { event.stop() },
            righties = {
                TextTooltip(text = strings[MEDIA_INFO]) {
                    IconButton(onClick = { event.setDialog(VmafDialogState.MediaInfo(reference))}) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = strings[MEDIA_INFO]
                        )
                    }
                }
            }
        )
        Row {
            TextField(
                value = distorted.toString(),
                //modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors().custom(),
                onValueChange = { },
                label = { Text(text = strings[DISTORTED_FILE_INPUT]) }
            )
            PrimaryButton(
                text = "Confirmar",
                onClick = {event.pickDistortedFile("Distorcido")}
            )
        }
        Row {
            TextField(
                value = model.toString(),
                //modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors().custom(),
                onValueChange = { },
                label = { Text(text = strings[MODEL_FILE_INPUT]) }
            )
            PrimaryButton(
                text = "Confirmar",
                onClick = {event.pickModelFile("MOdel")}
            )
        }
        Row {
            TextField(
                value = fps.toString(),
                colors = TextFieldDefaults.colors().custom(),
                onValueChange = { event.setFps(it) },
                label = { Text(text = strings[FPS_INPUT]) }
            )
            TextField(
                value = threads.toString(),
                colors = TextFieldDefaults.colors().custom(),
                onValueChange = { event.setThread(it) },
                label = { Text(text = strings[THREAD_INPUT]) }
            )
        }

        Progress(status)
    }
}

@Composable
    private fun Progress(status: VmafStatusState) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(height = dimens.xxl)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        if(status is VmafStatusState.Progress && status.percentage > 0f) {
            val text = "${String.format("%.2f", status.percentage)}% (${status.speed})"

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { status.percentage / 100 },
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
            )
            Spacer(modifier = Modifier.height(height = dimens.xs))
            Text(
                text = text,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        else if(status is VmafStatusState.Progress || status is VmafStatusState.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

private fun VmafState.canStart(): Boolean {
    return !(distorted.isNullOrBlank() ||
            model.isNullOrBlank() ||
            fps < 1 ||
            threads < 1 ||
            status is VmafStatusState.Loading ||
            status is VmafStatusState.Progress)
}

private fun VmafState.canStop(): Boolean = status is VmafStatusState.Progress