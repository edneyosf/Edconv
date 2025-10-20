package edneyosf.edconv.ui.components.alerts

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.previews.DarkPreview
import edneyosf.edconv.ui.previews.LightPreview

@Composable
fun ErrorAlertText(text: String) {
    AlertText(
        icon = Icons.Rounded.Error,
        color = MaterialTheme.colorScheme.error,
        text = text
    )
}

@Composable
fun WarningAlertText(text: String) {
    AlertText(
        icon = Icons.Rounded.Warning,
        color = MaterialTheme.colorScheme.tertiary,
        text = text
    )
}

@Composable
fun AlertText(icon: ImageVector, color: Color, text: String) {
    Row (verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.height(dimens.lg),
            imageVector = icon,
            tint = color,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(dimens.xs))
        Text(text = text, style = TextStyle(color = color))
    }
}

@Composable
private fun DefaultPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(dimens.xs)) {
        ErrorAlertText(text = "Sample")
        WarningAlertText(text = "Sample")
    }
}

@Preview
@Composable
private fun Light() = LightPreview { DefaultPreview() }

@Preview
@Composable
private fun Dark() = DarkPreview { DefaultPreview() }