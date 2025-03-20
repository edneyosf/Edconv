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
import features.home.events.HomeEvent
import features.home.events.HomeEvent.OnStart
import features.home.managers.HomeManager
import features.home.states.HomeState
import features.home.states.HomeStatus
import features.home.texts.HomeTexts
import features.home.texts.HomeTexts.Companion.SELECT_FILE_TEXT
import ui.compositions.dimens
import ui.compositions.languages
import ui.compositions.texts
import ui.previews.ScreenDelimiter

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val manager = remember { HomeManager(scope) }

    CompositionLocalProvider(texts provides HomeTexts(languages.current)) {
        HomeView(state = manager.state.value, onEvent = manager::onEvent)
    }
}

@Composable
private fun HomeView(state: HomeState, onEvent: (HomeEvent) -> Unit) {
    val status = state.status
    val scrollState = rememberScrollState()
    val modifier = Modifier
        .padding(dimens.current.wavelet)
        .verticalScroll(scrollState)

    Column(modifier = modifier) {
        Row {
            Button(onClick = {
                onEvent(HomeEvent.PickFile)
            }) {
                Text(texts.current.retrieve(SELECT_FILE_TEXT))
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

@Preview
@Composable
private fun Default() = ScreenDelimiter {
    HomeView(state = HomeManager.defaultState(), onEvent = {})
}