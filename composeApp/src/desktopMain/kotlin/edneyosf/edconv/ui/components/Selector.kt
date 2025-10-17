package edneyosf.edconv.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.previews.DarkPreview
import edneyosf.edconv.ui.previews.LightPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Selector(
    modifier: Modifier = Modifier, text: String, label: String, expanded: Boolean, enabled: Boolean = true,
    onExpanded: (Boolean) -> Unit, content: @Composable ColumnScope.() -> Unit) {

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { if(enabled) onExpanded(!expanded) },
    ) {
        TextField(
            value = text,
            readOnly = true,
            enabled = enabled,
            maxLines = 1,
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors().custom(),
            modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpanded(false) },
            content = content
        )
    }
}

@Composable
private fun DefaultPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(dimens.xs)) {
        Selector(
            text = "Sample",
            label = "Sample",
            expanded = true,
            onExpanded = { }
        ) {}
        Selector(
            text = "Sample",
            label = "Sample",
            enabled = false,
            expanded = true,
            onExpanded = { }
        ) {}
    }
}

@Preview
@Composable
private fun Light() = LightPreview { DefaultPreview() }

@Preview
@Composable
private fun Dark() = DarkPreview { DefaultPreview() }