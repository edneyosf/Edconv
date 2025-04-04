package ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.vector.ImageVector
import ui.components.texts.DialogTexts.Companion.CANCEL_BUTTON_TXT
import ui.components.texts.DialogTexts.Companion.CONFIRMATION_BUTTON_TXT
import ui.components.texts.dialogTexts
import ui.compositions.texts
import ui.compositions.textsComp

@Composable
fun SimpleDialog(
    title: String,
    description: String,
    icon: ImageVector,
    onConfirmation: () -> Unit,
    onCancel: (() -> Unit)? = null,
    onDismissRequest: () -> Unit
) {
    CompositionLocalProvider(textsComp provides dialogTexts) {
        AlertDialog(
            icon = { Icon(icon, contentDescription = null) },
            title = { Text(text = title) },
            text = { Text(text = description) },
            onDismissRequest = { onDismissRequest() },
            dismissButton = {
                onCancel?.let {
                    TextButton(
                        onClick = { it() }
                    ) {
                        Text(texts.get(CANCEL_BUTTON_TXT))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmation() }
                ) {
                    Text(texts.get(CONFIRMATION_BUTTON_TXT))
                }
            }
        )
    }
}