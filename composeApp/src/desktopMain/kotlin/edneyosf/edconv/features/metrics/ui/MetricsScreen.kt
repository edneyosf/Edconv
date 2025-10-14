package edneyosf.edconv.features.metrics.ui

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
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.mediainfo.MediaInfoScreen
import edneyosf.edconv.features.metrics.strings.MetricsScreenStrings.Keys.MEDIA_INFO
import edneyosf.edconv.features.metrics.strings.MetricsScreenStrings.Keys.*
import edneyosf.edconv.features.metrics.MetricsEvent
import edneyosf.edconv.features.metrics.states.MetricsState
import edneyosf.edconv.features.metrics.states.MetricsStatusState
import edneyosf.edconv.features.metrics.strings.metricsScreenStrings
import edneyosf.edconv.features.metrics.MetricsViewModel
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
import java.io.File

@Composable
fun MetricsScreen() {
    val viewModel = koinViewModel<MetricsViewModel>()
    val state by viewModel.state

    CompositionLocalProvider(value = stringsComp provides metricsScreenStrings) {
        state.Dialogs(event = viewModel)
        state.Content(
            event = viewModel
        )
    }
}

@Composable
private fun MetricsState.Content(event: MetricsEvent) {
    val stringPickFile = strings[TITLE_PICK_FILE]
    var showMediaInfo by remember { mutableStateOf(value = false) }

    if(showMediaInfo) {
        reference?.MediaInfoScreen(
            onFinish = { showMediaInfo = false }
        )
    }

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
                            onClick = { showMediaInfo = true }
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
        Row {
            CheckboxInput(
                checked = vmaf,
                label = strings[VMAF],
                onCheckedChange = event::setVmaf
            )
            CheckboxInput(
                checked = psnr,
                label = strings[PSNR],
                onCheckedChange = event::setPsnr
            )
            CheckboxInput(
                checked = ssim,
                label = strings[SSIM],
                onCheckedChange = event::setSsim
            )
        }
        Row {
            TextField(
                modifier = Modifier.width(width = 164.dp),
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
                modifier = Modifier.width(width = 148.dp),
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            val distortedFile = distorted?.let { File(it) }

            TextField(
                modifier = Modifier.weight(weight = 1f),
                value = distortedFile?.name ?: "",
                colors = TextFieldDefaults.colors().custom(),
                placeholder = { Text(text = "video.mkv") },
                readOnly = true,
                maxLines = 1,
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
    }
}

@Composable
    private fun Progress(status: MetricsStatusState) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(height = dimens.xxl)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        if(status is MetricsStatusState.Progress && status.percentage > 0f) {
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
        else if(status is MetricsStatusState.Progress || status is MetricsStatusState.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun CheckboxInput(checked: Boolean, label: String, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(width = dimens.xxs))
        Text(
            text = label,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

private fun MetricsState.canStart(): Boolean {
    return !(distorted.isNullOrBlank() ||
            fps < 1 ||
            threads < 1 ||
            !vmaf && !psnr && !ssim ||
            status is MetricsStatusState.Loading ||
            status is MetricsStatusState.Progress)
}

private fun MetricsState.canStop(): Boolean = status is MetricsStatusState.Progress

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides metricsScreenStrings) {
        val type = MediaType.VIDEO
        val input = InputMedia(
            path = "/dir/video.mkv",
            type = type,
            size = 123456L,
            sizeText = "123456",
            duration = 123456L,
            durationText = "123456"
        )
        val progressStatus = MetricsStatusState.Progress(
            percentage = 50.0f,
            speed = "1.6x"
        )
        val state = MetricsState(
            status = progressStatus,
            reference = input,
            distorted = "/dir/video.mkv"
        )

        state.Content(event = object : MetricsEvent {})
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