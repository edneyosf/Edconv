package edneyosf.edconv.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.previews.DarkPreview
import edneyosf.edconv.ui.previews.LightPreview

@Composable
fun ActionsTool(
    startEnabled: Boolean,
    stopEnabled: Boolean,
    startDescription: String,
    stopDescription: String,
    righties: @Composable (() -> Unit)? = null,
    lefties: @Composable (() -> Unit)? = null,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    val modifier =  Modifier
        .fillMaxWidth()
        .padding(bottom = dimens.sm)

    Row(modifier = modifier) {
        if(lefties != null) lefties()
        Spacer(modifier = Modifier.weight(weight = 1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledIconButton(
                enabled = startEnabled,
                onClick = onStart
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = startDescription
                )
            }
            Text(
                text = startDescription,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.width(width = dimens.md))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledTonalIconButton(
                enabled = stopEnabled,
                onClick = onStop
            ) {
                Icon(
                    imageVector = Icons.Rounded.Stop,
                    contentDescription = stopDescription
                )
            }
            Text(
                text = stopDescription,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.weight(weight = 1f))
        if(righties != null) righties()
    }
}

@Composable
private fun DefaultPreview() {
    ActionsTool(
        startEnabled = true,
        stopEnabled = false,
        startDescription = "Start",
        stopDescription = "Stop",
        righties = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null
                )
            }
        },
        lefties = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null
                )
            }
        },
        onStart = {},
        onStop = {}
    )
}

@Preview
@Composable
private fun Light() = LightPreview { DefaultPreview() }

@Preview
@Composable
private fun Dark() = DarkPreview { DefaultPreview() }