package edneyosf.edconv.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.vector.ImageVector
import edneyosf.edconv.ui.components.texts.DialogTexts.Companion.CANCEL_BUTTON_TXT
import edneyosf.edconv.ui.components.texts.DialogTexts.Companion.CONFIRMATION_BUTTON_TXT
import edneyosf.edconv.ui.components.texts.dialogTexts
import edneyosf.edconv.ui.compositions.texts
import edneyosf.edconv.ui.compositions.textsComp

@Composable
fun SimpleDialog(
    title: String,
    content: @Composable (() -> Unit),
    icon: ImageVector,
    confirmationButtonEnabled: Boolean = true,
    onConfirmation: () -> Unit,
    onCancel: (() -> Unit)? = null,
    onDismissRequest: () -> Unit
) {
    CompositionLocalProvider(textsComp provides dialogTexts) {
        AlertDialog(
            icon = { Icon(icon, contentDescription = null) },
            title = { Text(text = title) },
            text = content,
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
                    enabled = confirmationButtonEnabled,
                    onClick = { onConfirmation() }
                ) {
                    Text(texts.get(CONFIRMATION_BUTTON_TXT))
                }
            }
        )
    }
}