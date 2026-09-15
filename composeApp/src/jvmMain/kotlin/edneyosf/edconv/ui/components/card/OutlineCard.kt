package edneyosf.edconv.ui.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.previews.DarkPreview
import edneyosf.edconv.ui.previews.LightPreview
import edneyosf.edconv.ui.theme.onSurfaceDark

@Composable
fun OutlineCard(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(
            width = 1.dp,
            color = onSurfaceDark.copy(alpha = 0.10f)
        )
    ) {
        Column(
            modifier = Modifier.padding(all = dimens.md),
            content = content
        )
    }
}

@Composable
private fun OutlineCardPreview() {
    OutlineCard {
        Text("Sample")
    }
}

@Preview
@Composable
private fun LightOutlineCard() {
    LightPreview {
        OutlineCardPreview()
    }
}

@Preview
@Composable
private fun DarkOutlineCard() {
    DarkPreview {
        OutlineCardPreview()
    }
}