package edneyosf.edconv.ui.components.buttons

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.theme.AppTheme

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.heightIn(dimens.xxl),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(horizontal = dimens.sm),
        enabled = enabled && !loading,
        onClick = onClick
    ) {
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
private fun DefaultPreview(darkTheme: Boolean) {
    AppTheme(darkTheme = darkTheme) {
        Surface {
            Column(
                Modifier.padding(all = dimens.md),
                verticalArrangement = Arrangement.spacedBy(dimens.xs)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(dimens.xs)) {
                    PrimaryButton(text = "Sample", onClick = {})
                    PrimaryButton(icon = Icons.Rounded.CheckCircle, text = "Sample", onClick = {})
                }
                Row(horizontalArrangement = Arrangement.spacedBy(dimens.xs)) {
                    PrimaryButton(text = "Sample", enabled = false, onClick = {})
                    PrimaryButton(icon = Icons.Rounded.CheckCircle, text = "Sample", enabled = false, onClick = {})
                    PrimaryButton(icon = Icons.Rounded.CheckCircle, text = "Sample", loading = true, onClick = {})
                }
            }
        }
    }
}

@Preview
@Composable
private fun LightPreview() {
    DefaultPreview(darkTheme = false)
}

@Preview
@Composable
private fun DarkPreview() {
    DefaultPreview(darkTheme = true)
}