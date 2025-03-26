package ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ui.theme.extensions.custom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Selector(
    text: String, label: String, expanded: Boolean, onExpanded: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit) {

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpanded(!expanded) },
    ) {
        TextField(
            value = text,
            readOnly = true,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors().custom(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpanded(false) },
            content = content
        )
    }
}