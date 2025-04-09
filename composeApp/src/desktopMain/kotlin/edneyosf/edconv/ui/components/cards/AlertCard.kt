package edneyosf.edconv.ui.components.cards

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
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
fun ErrorAlertCard(text: String) {
    AlertCard(
        icon = Icons.Rounded.Error,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        text = text
    )
}

@Composable
fun WarningAlertCard(text: String) {
    AlertCard(
        icon = Icons.Rounded.Info,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        text = text
    )
}

@Composable
fun AlertCard(icon: ImageVector, containerColor: Color, contentColor: Color, text: String) {
    Card(colors = CardDefaults.cardColors().copy(containerColor = containerColor)) {
        Row (modifier = Modifier.padding(dimens.sm), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.height(dimens.lg),
                imageVector = icon,
                tint = contentColor,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(dimens.xs))
            Text(text = text, style = TextStyle(color = contentColor))
        }
    }
}

@Preview
@Composable
private fun ErrorLight() = LightPreview { ErrorAlertCard(text = "Sample") }

@Preview
@Composable
private fun ErrorDark() = DarkPreview { ErrorAlertCard(text = "Sample") }

@Preview
@Composable
private fun WarningLight() = LightPreview { WarningAlertCard(text = "Sample") }

@Preview
@Composable
private fun WarningDark() = DarkPreview { WarningAlertCard(text = "Sample") }