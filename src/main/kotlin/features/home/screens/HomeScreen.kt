package features.home.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.Languages
import edconv.common.MediaFormat
import features.home.events.HomeEvent
import features.home.events.HomeEvent.OnStart
import features.home.events.HomeEvent.OnStop
import features.home.events.HomeEvent.SetOutputFile
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
            logs = manager.logs.value,
            onEvent = manager::onEvent
        )
    }
}

@Composable
private fun HomeView(state: HomeState, logs: String, onEvent: (HomeEvent) -> Unit) {
    val status = state.status
    val scrollState = rememberScrollState()
    val title = texts.get(TITLE_PICK_FILE_TEXT)
    val modifier = Modifier
        .padding(dimens.i)
        .verticalScroll(scrollState)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(dimens.d)) {

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(dimens.d)) {
            Button(onClick = {
                pickFile(title)?.let { onEvent(HomeEvent.SetInputFile(it)) }
            }) {
                Text(texts.get(SELECT_FILE_TEXT))
            }
            Text(state.inputFile.toString())
        }

        HorizontalDivider()

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            IconButton(
                enabled = status !is HomeStatus.Loading && status !is HomeStatus.Progress,
                onClick = { onEvent(OnStart) }){
                Icon(imageVector = Icons.Filled.PlayArrow, tint = if(status is HomeStatus.Loading || status is HomeStatus.Progress) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.primary, contentDescription = null)
            }
            IconButton(onClick = { onEvent(OnStop) }){
                Icon(imageVector = Icons.Filled.Stop, tint = MaterialTheme.colorScheme.primary, contentDescription = null)
            }
        }

        HorizontalDivider()

        Row {
            MenuComTextField()
        }

        HorizontalDivider()

        Row {
            TextField(
                value = state.outputFile,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onEvent(SetOutputFile(it)) },
                label = { Text("Output") }
            )
        }

        HorizontalDivider()

        Row {
            Card(modifier = Modifier.fillMaxWidth().height(150.dp).verticalScroll(rememberScrollState())) {
                Text(logs)
            }
        }

        HorizontalDivider()

        Column(verticalArrangement = Arrangement.spacedBy(dimens.d), horizontalAlignment = Alignment.CenterHorizontally) {
            when(status) {
                is HomeStatus.Loading -> Progress()
                is HomeStatus.Progress -> {
                    Progress(status.percentage)
                    Text("${String.format("%.2f", status.percentage)}%")
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun Progress(percentage: Float = 0f) {
    LinearProgressIndicator(
        progress = { percentage / 100 },
        modifier = Modifier.fillMaxWidth(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuComTextField() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(MediaFormat.AAC) }
    val options = listOf(MediaFormat.AAC, MediaFormat.EAC3, MediaFormat.H265, MediaFormat.AV1)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedOption.text,
            readOnly = true,
            onValueChange = {  },
            label = { Text("Formato") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).width(140.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.text) },
                    onClick = {
                        selectedOption = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }
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
        HomeView(state = HomeManager.defaultState(),"", onEvent = {})
    }
}

@Preview
@Composable
private fun Portuguese() = ScreenDelimiter {
    CompositionLocalProvider(textsComp provides HomeTexts(Languages.PT)) {
        HomeView(state = HomeManager.defaultState(), "", onEvent = {})
    }
}