package edneyosf.edconv.ui.components.buttons

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.previews.DarkPreview
import edneyosf.edconv.ui.previews.LightPreview

@Composable
fun SecondaryButton(
    icon: ImageVector? = null, text: String, enabled: Boolean = true, loading: Boolean = false, onClick: () -> Unit) {

    FilledTonalButton(enabled = enabled && !loading, onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (loading || icon != null) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimens.ml),
                        strokeWidth = 2.dp
                    )
                } else {
                    icon?.let {
                        Icon(
                            modifier = Modifier.height(dimens.ml),
                            imageVector = it,
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.width(dimens.xs))
            }
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun DefaultPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(dimens.xs)) {
        Row(horizontalArrangement = Arrangement.spacedBy(dimens.xs)) {
            SecondaryButton(text = "Sample", onClick = {})
            SecondaryButton(icon = Icons.Rounded.CheckCircle, text = "Sample", onClick = {})
        }
        Row(horizontalArrangement = Arrangement.spacedBy(dimens.xs)) {
            SecondaryButton(text = "Sample", enabled = false, onClick = {})
            SecondaryButton(icon = Icons.Rounded.CheckCircle, text = "Sample", enabled = false, onClick = {})
            SecondaryButton(icon = Icons.Rounded.CheckCircle, text = "Sample", loading = true, onClick = {})
        }
    }
}

@Preview
@Composable
private fun Light() = LightPreview { DefaultPreview() }

@Preview
@Composable
private fun Dark() = DarkPreview { DefaultPreview() }