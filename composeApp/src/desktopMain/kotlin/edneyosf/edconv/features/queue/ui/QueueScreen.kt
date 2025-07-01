package edneyosf.edconv.features.queue.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import edconv.composeapp.generated.resources.Res
import edconv.composeapp.generated.resources.icon
import edneyosf.edconv.core.extensions.toReadableCommand
import edneyosf.edconv.core.process.MediaQueue
import edneyosf.edconv.core.process.QueueStatus
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.queue.QueueEvent
import edneyosf.edconv.features.queue.QueueViewModel
import edneyosf.edconv.features.queue.strings.QueueScreenStrings.Keys.*
import edneyosf.edconv.features.queue.strings.queueScreenStrings
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ui.components.buttons.PrimaryButton
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

    Scaffold { innerPadding ->
        Row(modifier = Modifier.padding(paddingValues = innerPadding)) {
            Column(modifier = Modifier.weight(weight = 2f)) {
                LazyColumn(modifier = Modifier.weight(weight = 1f)) {
                    items(items = this@Content) { item ->
                        QueueItem(
                            selected = selected?.id == item.id,
                            mediaType = item.type,
                            fileName = item.outputFile.name,
                            status = item.status,
                            onClick = { selected = item },
                            onRemove = { event.removeItem(item) }
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    PrimaryButton(icon = Icons.Rounded.Delete, text = "Limpar", onClick = { event.clear() })
                }
            }
            VerticalDivider()
            Column(modifier = Modifier.weight(weight = 1f).padding(8.dp)) {
                Text(selected?.input?.path ?: "", style = TextStyle(MaterialTheme.colorScheme.onSurface))
                Spacer(modifier = Modifier.height(8.dp))
                Text("${selected?.command?.toReadableCommand()}", style = TextStyle(MaterialTheme.colorScheme.onSurface))
                Spacer(modifier = Modifier.height(8.dp))
                Text(selected?.outputFile?.path ?: "", style = TextStyle(MaterialTheme.colorScheme.onSurface))
            }
        }
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
        type = MediaType.AUDIO
    ))

    queue.Content(event = object : QueueEvent {})
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