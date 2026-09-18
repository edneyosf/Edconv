package edneyosf.edconv.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.previews.DarkPreview
import edneyosf.edconv.ui.previews.LightPreview
import edneyosf.edconv.ui.theme.AppTheme

@Composable
fun CheckboxField(
    modifier: Modifier = Modifier,
    checked: Boolean,
    title: String,
    subtitle: String? = null,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            modifier = Modifier.scale(0.8f),
            checked = checked,
            onCheckedChange = onCheckedChange
        )

        Column {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            subtitle?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CheckboxFieldPreview(darkTheme: Boolean) {
    AppTheme(darkTheme = darkTheme) {
        Surface {
            Column(
                Modifier.padding(all = dimens.md),
                verticalArrangement = Arrangement.spacedBy(dimens.xs)
            ) {
                CheckboxField(
                    checked = true,
                    title = "Accept terms and conditions",
                    onCheckedChange = {  }
                )
                CheckboxField(
                    checked = true,
                    title = "Accept terms and conditions",
                    subtitle = "By clicking this checkbox, you agree to the terms.",
                    onCheckedChange = {  }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Light() {
    LightPreview {
        CheckboxFieldPreview(darkTheme = false)
    }
}

@Preview
@Composable
private fun Dark() {
    DarkPreview {
        CheckboxFieldPreview(darkTheme = true)
    }
}