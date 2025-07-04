package edneyosf.edconv.features.queue.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Queue
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Window
import edconv.composeapp.generated.resources.Res
import edconv.composeapp.generated.resources.icon
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.extensions.toReadableCommand
import edneyosf.edconv.core.process.MediaQueue
import edneyosf.edconv.core.process.QueueStatus
import edneyosf.edconv.features.common.CommonStrings.Keys.ERROR_DEFAULT
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.CONVERSION_PROCESS
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.CONVERSION_PROCESS_COMPLETED
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.FILE_ALREADY_EXISTS
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.INPUT_FILE_NOT_EXIST
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.INPUT_NOT_FILE
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.ON_ADD_TO_QUEUE
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.ON_ADD_TO_QUEUE_REQUIREMENTS
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.ON_STOPPING_CONVERSION
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.PROCESS_NULL
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.START_TIME_NULL
import edneyosf.edconv.features.queue.QueueEvent
import edneyosf.edconv.features.queue.QueueViewModel
import edneyosf.edconv.features.queue.strings.QueueScreenStrings.Keys.*
import edneyosf.edconv.features.queue.strings.queueScreenStrings
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ui.components.InfoScreen
import edneyosf.edconv.ui.components.buttons.PrimaryButton
import edneyosf.edconv.ui.components.extensions.customColor
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import java.io.File

@Composable
fun QueueScreen(onClose: () -> Unit) {
    val viewModel = koinViewModel<QueueViewModel>()
    val state by viewModel.state.collectAsState()

    CompositionLocalProvider(value = stringsComp provides queueScreenStrings) {
        Window(
            onCloseRequest = onClose,
            title = strings[WINDOW_TITLE],
            icon = painterResource(resource = Res.drawable.icon)
        ) {
            state.Content(event = viewModel)
        }
    }
}

@Composable
private fun List<MediaQueue>.Content(event: QueueEvent) {
    var selected by remember { mutableStateOf(value = firstOrNull()) }

    LaunchedEffect(this) {
        selected = if(none { it.id == selected?.id }) firstOrNull()
        else firstOrNull { it.id == selected?.id }
    }

    Scaffold { innerPadding ->
        if(isNotEmpty()) {
            Row(modifier = Modifier.padding(paddingValues = innerPadding)) {
                Column(modifier = Modifier.weight(weight = 1f)) {
                    LazyColumn(modifier = Modifier.weight(weight = 1f)) {
                        items(items = this@Content) {
                            QueueItem(
                                selected = selected?.id == it.id,
                                mediaType = it.type,
                                fileName = it.outputFile.name,
                                status = it.status,
                                onClick = { selected = it },
                                onRemove = { event.removeItem(item = it) }
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimens.md)
                    ) {
                        PrimaryButton(
                            icon = Icons.Rounded.Delete,
                            text = strings[CLEAN],
                            onClick = event::clear
                        )
                    }
                }
                selected?.let {
                    VerticalDivider(color = DividerDefaults.customColor())
                    it.Details(modifier = Modifier.weight(weight = 1.25f))
                }
            }
        }
        else {
            InfoScreen(
                icon = Icons.Rounded.Queue,
                description = strings[EMPTY]
            )
        }
    }
}

@Composable
private fun MediaQueue.Details(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = dimens.md),
        modifier = modifier
            .padding(all = dimens.md)
            .verticalScroll(state = rememberScrollState())
    ) {
        DetailItem(
            label = strings[SOURCE],
            value = input.path
        )
        DetailItem(
            label = strings[OUTPUT],
            value = outputFile.path
        )
        error?.let {
            val description = when(it) {
                Error.ON_ADD_TO_QUEUE -> strings[ON_ADD_TO_QUEUE]
                Error.ON_ADD_TO_QUEUE_REQUIREMENTS -> strings[ON_ADD_TO_QUEUE_REQUIREMENTS]
                Error.ON_STOPPING_CONVERSION -> strings[ON_STOPPING_CONVERSION]
                Error.START_TIME_NULL -> strings[START_TIME_NULL]
                Error.INPUT_FILE_NOT_EXIST -> strings[INPUT_FILE_NOT_EXIST]
                Error.INPUT_NOT_FILE -> strings[INPUT_NOT_FILE]
                Error.CONVERSION_PROCESS_COMPLETED -> strings[CONVERSION_PROCESS_COMPLETED]
                Error.PROCESS_NULL -> strings[PROCESS_NULL]
                Error.CONVERSION_PROCESS -> strings[CONVERSION_PROCESS]
                Error.FILE_ALREADY_EXISTS -> strings[FILE_ALREADY_EXISTS]
                else -> commonStrings[ERROR_DEFAULT]
            }

            DetailItem(
                label = strings[ERROR],
                value = description
            )
        }
        startTime?.let {
            DetailItem(
                label = strings[START_TIME],
                value = it
            )
        }
        finishTime?.let {
            DetailItem(
                label = strings[END_TIME],
                value = it
            )
        }
        duration?.let {
            DetailItem(
                label = strings[DURATION_TIME],
                value = it.toReadableCommand()
            )
        }
        DetailItem(
            label = strings[COMMAND],
            value = command.toReadableCommand()
        )
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = "$label: ",
            style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
            text = value,
            style = TextStyle(color = MaterialTheme.colorScheme.onSurface)
        )
    }
}

@Composable
private fun DefaultPreview() {
    val queue = mutableListOf<MediaQueue>()
    val filePath = "/Dir/File.ed"
    val outputFile = File(filePath)
    val input = InputMedia(
        path = filePath,
        type = MediaType.VIDEO,
        size = 123456L,
        sizeText = "123456",
        duration = 123456L,
        durationText = "123456"
    )
    val item = MediaQueue(
        id = "1",
        status = QueueStatus.NOT_STARTED,
        input = input,
        type = MediaType.VIDEO,
        command = "Command",
        startTime = "12:00",
        finishTime = "22:00",
        duration = "2:00",
        outputFile = outputFile
    )

    queue.add(item)
    queue.add(item.copy(
        id = "2",
        status = QueueStatus.STARTED,
        type = MediaType.AUDIO
    ))
    queue.add(item.copy(
        id = "3",
        status = QueueStatus.IN_PROGRESS
    ))
    queue.add(item.copy(
        id = "4",
        status = QueueStatus.FINISHED
    ))
    queue.add(item.copy(
        id = "5",
        status = QueueStatus.ERROR,
        error = Error.DEFAULT,
        type = MediaType.AUDIO
    ))

    CompositionLocalProvider(value = stringsComp provides queueScreenStrings) {
        queue.Content(event = object : QueueEvent {})
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