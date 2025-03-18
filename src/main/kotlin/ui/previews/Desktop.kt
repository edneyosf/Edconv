package ui.previews

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val WIDTH_DEFAULT = 800f
private const val HEIGHT_DEFAULT = 600f

@Composable
fun Desktop(width: Dp = WIDTH_DEFAULT.dp, height: Dp = HEIGHT_DEFAULT.dp, content: @Composable (BoxScope.() -> Unit)) {
    val modifier = Modifier
        .size(width = width, height = height)
        .border(width = 1.dp, color = Color.Black)

    MaterialTheme { Box(modifier = modifier, content = content) }
}