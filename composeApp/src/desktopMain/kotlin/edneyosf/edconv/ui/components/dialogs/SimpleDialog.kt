package edneyosf.edconv.ui.components.dialogs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import edneyosf.edconv.ui.previews.DarkPreview
import edneyosf.edconv.ui.previews.LightPreview

@Composable
fun SimpleDialog(
    icon: ImageVector,
    title: String,
    content: @Composable (() -> Unit),
    confirmationEnabled: Boolean = true,
    confirmationText: String,
    cancelEnabled: Boolean = true,
    cancelText: String? = null,
    onConfirmation: () -> Unit,
    onCancel: (() -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null
) {
    AlertDialog(
        icon = { Icon(icon, contentDescription = null) },
        title = { Text(text = title) },
        text = content,
        onDismissRequest = { onDismissRequest?.invoke() },
        dismissButton = {
            cancelText?.let {
                TextButton(
                    enabled = cancelEnabled,
                    onClick = { onCancel?.invoke() },
                    content = { Text(it) }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = confirmationEnabled,
                onClick = { onConfirmation() },
                content = { Text(confirmationText) }
            )
        }
    )
}

@Composable
private fun DefaultPreview() {
    SimpleDialog(
        icon = Icons.Rounded.Info,
        title = "Sample",
        content = { Text("Sample") },
        confirmationText = "Confirm",
        cancelText = "Cancel",
        onConfirmation = {},
        onCancel = {},
        onDismissRequest = {}
    )
}

@Preview
@Composable
private fun Light() = LightPreview { DefaultPreview() }

@Preview
@Composable
private fun Dark() = DarkPreview { DefaultPreview() }