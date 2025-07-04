package edneyosf.edconv.features.vmaf.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileOpen
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
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.vmaf.strings.VmafScreenStrings.Keys.MEDIA_INFO
import edneyosf.edconv.features.vmaf.strings.VmafScreenStrings.Keys.*
import edneyosf.edconv.features.vmaf.VmafEvent
import edneyosf.edconv.features.vmaf.states.VmafDialogState
import edneyosf.edconv.features.vmaf.states.VmafState
import edneyosf.edconv.features.vmaf.states.VmafStatusState
import edneyosf.edconv.features.vmaf.strings.vmafScreenStrings
import edneyosf.edconv.features.vmaf.VmafViewModel
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ui.components.ActionsTool
import edneyosf.edconv.ui.components.TextTooltip
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VMAFScreen() {
    val viewModel = koinViewModel<VmafViewModel>()
    val state by viewModel.state

    CompositionLocalProvider(value = stringsComp provides vmafScreenStrings) {
        state.Dialogs(event = viewModel)
        state.Content(
            event = viewModel
        )
    }
}

@Composable
private fun VmafState.Content(event: VmafEvent) {
    val stringPickFile = strings[TITLE_PICK_FILE]

    Column(
        modifier = Modifier.padding(all = dimens.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = dimens.xl)
    ) {
        ActionsTool(
            startEnabled = canStart(),
            stopEnabled = canStop(),
            startDescription = strings[START_ANALYSIS],
            stopDescription = strings[STOP_ANALYSIS],
            onStart = event::start,
            onStop = event::stop,
            righties = {
                reference?.let {
                    TextTooltip(text = strings[MEDIA_INFO]) {
                        IconButton(
                            onClick = { event.setDialog(VmafDialogState.MediaInfo(inputMedia = it)) }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = strings[MEDIA_INFO]
                            )
                        }
                    }
                }
            }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier.weight(weight = 1f),
                value = distorted ?: "",
                colors = TextFieldDefaults.colors().custom(),
                placeholder = { Text(text = "video.mkv") },
                readOnly = true,
                singleLine = true,
                onValueChange = {},
                label = { Text(text = strings[DISTORTED_FILE_INPUT]) }
            )
            Spacer(modifier = Modifier.width(width = dimens.xs))
            TextTooltip(text = strings[SELECT_FILE]) {
                IconButton(
                    onClick = { event.pickDistortedFile(title = stringPickFile) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FileOpen,
                        contentDescription = strings[SELECT_FILE]
                    )
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier.weight(weight = 1f),
                value = model ?: "",
                colors = TextFieldDefaults.colors().custom(),
                onValueChange = { },
                singleLine = true,
                readOnly = true,
                placeholder = { Text(text = "model.json") },
                label = { Text(text = strings[MODEL_FILE_INPUT]) }
            )
            Spacer(modifier = Modifier.width(width = dimens.xs))
            TextTooltip(text = strings[SELECT_FILE]) {
                IconButton(
                    onClick = { event.pickModelFile(title = stringPickFile) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FileOpen,
                        contentDescription = strings[SELECT_FILE]
                    )
                }
            }
        }
        Row {
            TextField(
                value = fps.toString(),
                colors = TextFieldDefaults.colors().custom(),
                singleLine = true,
                onValueChange = {
                    val value = it.toIntOrNull()

                    if(value != null && value > 0 && it.length <= 3) {
                        event.setFps(it)
                    }
                },
                label = { Text(text = strings[FPS_INPUT]) }
            )
            Spacer(modifier = Modifier.width(width = dimens.xl))
            TextField(
                value = threads.toString(),
                colors = TextFieldDefaults.colors().custom(),
                singleLine = true,
                onValueChange = {
                    val value = it.toIntOrNull()

                    if(value != null && value > 0 && it.length <= 3) {
                        event.setThread(it)
                    }
                },
                label = { Text(text = strings[THREAD_INPUT]) }
            )
        }
        Spacer(modifier = Modifier.weight(weight = 1f))
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

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides vmafScreenStrings) {
        val type = MediaType.VIDEO
        val input = InputMedia(
            path = "/dir/video.mkv",
            type = type,
            size = 123456L,
            sizeText = "123456",
            duration = 123456L,
            durationText = "123456"
        )
        val progressStatus = VmafStatusState.Progress(
            percentage = 50.0f,
            speed = "1.6x"
        )
        val state = VmafState(
            status = progressStatus,
            reference = input,
            distorted = "/dir/video.mkv",
            model = "/dir/model.json"
        )

        state.Content(event = object : VmafEvent {})
    }
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