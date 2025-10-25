package edneyosf.edconv.features.console.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edconv.composeapp.generated.resources.Res
import edconv.composeapp.generated.resources.icon
import edneyosf.edconv.app.AppConfigs.MIN_SUB_WINDOW_HEIGHT
import edneyosf.edconv.app.AppConfigs.MIN_SUB_WINDOW_WIDTH
import edneyosf.edconv.features.console.ConsoleEvent
import edneyosf.edconv.features.console.strings.ConsoleScreenStrings.Keys.*
import edneyosf.edconv.features.console.ConsoleViewModel
import edneyosf.edconv.features.console.strings.consoleScreenStrings
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import edneyosf.edconv.ui.theme.firaCodeFont
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import java.awt.Dimension

@Composable
fun ConsoleScreen(onFinish: () -> Unit) {
    val viewModel = koinViewModel<ConsoleViewModel>()
    val logs by viewModel.logsState.collectAsStateWithLifecycle()
    val command by viewModel.commandState.collectAsStateWithLifecycle()

    CompositionLocalProvider(value = stringsComp provides consoleScreenStrings) {
        Window(
            icon = painterResource(resource = Res.drawable.icon),
            title = strings[TITLE],
            content = {
                window.minimumSize = Dimension(MIN_SUB_WINDOW_WIDTH, MIN_SUB_WINDOW_HEIGHT)
                Content(logs, command, event = viewModel)
            },
            onCloseRequest = onFinish
        )
    }
}

@Composable
private fun Content(logs: List<String>, command: String, event: ConsoleEvent) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = dimens.md)
        ) {
            if(logs.isNotEmpty()) LogsView(data = logs)
            else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxWidth()
                        .padding(all = dimens.md)
                ) {
                    Spacer(modifier = Modifier.weight(weight = 1f))
                    Icon(
                        modifier = Modifier.size(size = dimens.xxl),
                        imageVector = Icons.Rounded.Description,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(height = dimens.xs))
                    Text(text = strings[EMPTY_LOGS])
                    Spacer(modifier = Modifier.weight(weight = 1f))
                }
            }
            Spacer(modifier = Modifier.height(height = dimens.sm))
            TextField(
                value = command,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                colors = TextFieldDefaults.colors().custom(),
                onValueChange = event::setCommand,
                label = { Text(text = strings[COMMAND_INPUT]) }
            )
        }
    }
}

@Composable
private fun ColumnScope.LogsView(data: List<String>) {
    val scrollState = rememberLazyListState()
    val modifier = Modifier
        .fillMaxWidth()
        .weight(weight = 1f)
        .padding(end = dimens.sm)

    LaunchedEffect(data.size) {
        if(data.isNotEmpty()) {
            scrollState.animateScrollToItem(index = data.size - 1)
        }
    }

    Box(modifier = modifier) {
        SelectionContainer(
            modifier = Modifier
        ) {
            LazyColumn(state = scrollState) {
                items(items = data) {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            fontFamily = firaCodeFont
                        ),
                        modifier = Modifier.padding(bottom = dimens.md)
                    )
                }
            }
        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            style = LocalScrollbarStyle.current.copy(
                hoverColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides consoleScreenStrings) {
        Content(
            logs = listOf("Sample", "Sample"),
            command = "Command",
            event = object : ConsoleEvent {}
        )
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