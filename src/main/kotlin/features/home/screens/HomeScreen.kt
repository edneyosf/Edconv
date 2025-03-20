package features.home.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import core.Languages
import features.home.events.HomeEvent
import features.home.events.HomeEvent.OnStart
import features.home.managers.HomeManager
import features.home.states.HomeState
import features.home.states.HomeStatus
import features.home.texts.HomeTexts
import features.home.texts.HomeTexts.Companion.SELECT_FILE_TEXT
import features.home.texts.HomeTexts.Companion.TITLE_PICK_FILE_TEXT
import features.home.texts.homeTexts
import ui.compositions.*
import ui.previews.ScreenDelimiter
import java.awt.FileDialog
import java.awt.Frame

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val manager = remember { HomeManager(scope) }

    CompositionLocalProvider(textsComp provides homeTexts) {
        HomeView(
            state = manager.state.value,
            onEvent = manager::onEvent
        )
    }
}

@Composable
private fun HomeView(state: HomeState, onEvent: (HomeEvent) -> Unit) {
    val status = state.status
    val scrollState = rememberScrollState()
    val title = texts.get(TITLE_PICK_FILE_TEXT)
    val modifier = Modifier
        .padding(dimens.i)
        .verticalScroll(scrollState)

    Column(modifier = modifier) {
        Row {
            Button(onClick = {
                pickFile(title)?.let { onEvent(HomeEvent.SetInputFile(it)) }
            }) {
                Text(texts.get(SELECT_FILE_TEXT))
            }
            IconButton(onClick = { onEvent(OnStart) }){
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
            }
        }

        when(status) {
            is HomeStatus.Loading -> Progress()
            is HomeStatus.Progress -> Progress(status.percentage)
            else -> Unit
        }
    }
}

@Composable
private fun Progress(percentage: Float = 0f) {
    LinearProgressIndicator(
        progress = percentage / 100,
        modifier = Modifier.fillMaxWidth(),
    )
}

private fun pickFile(title: String): String? {
    val dialog = FileDialog(null as Frame?, title, FileDialog.LOAD)
        .apply { isVisible = true }
    val file = dialog.files.firstOrNull()

    return file?.absolutePath
}

@Preview
@Composable
private fun English() = ScreenDelimiter {
    CompositionLocalProvider(textsComp provides HomeTexts(Languages.EN)) {
        HomeView(state = HomeManager.defaultState(), onEvent = {})
    }
}

@Preview
@Composable
private fun Portuguese() = ScreenDelimiter {
    CompositionLocalProvider(textsComp provides HomeTexts(Languages.PT)) {
        HomeView(state = HomeManager.defaultState(), onEvent = {})
    }
}