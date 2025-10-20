package edneyosf.edconv.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Queue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.previews.DarkPreview
import edneyosf.edconv.ui.previews.LightPreview

@Composable
fun InfoScreen(icon: ImageVector, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = dimens.md)
    ) {
        Spacer(modifier = Modifier.weight(weight = 1f))
        Icon(
            modifier = Modifier.size(size = dimens.giga),
            imageVector = icon,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(height = dimens.xl))
        Text(text = description)
        Spacer(modifier = Modifier.weight(weight = 1f))
    }
}

@Composable
private fun DefaultPreview() {
    InfoScreen(
        icon = Icons.Rounded.Queue,
        description = "Description"
    )
}

@Preview
@Composable
private fun Light() = LightPreview { DefaultPreview() }

@Preview
@Composable
private fun Dark() = DarkPreview { DefaultPreview() }