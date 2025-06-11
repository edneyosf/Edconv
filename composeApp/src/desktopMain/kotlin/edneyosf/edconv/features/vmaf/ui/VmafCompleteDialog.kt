package edneyosf.edconv.features.vmaf.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edneyosf.edconv.features.common.CommonStrings.Keys.CONFIRMATION_BUTTON
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.vmaf.strings.VmafCompleteDialogStrings.Keys.*
import edneyosf.edconv.features.vmaf.strings.vmafCompleteDialogStrings
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun VmafCompleteDialog(score: String, startTime: String, finishTime: String, duration: String, onFinish: () -> Unit) {
    CompositionLocalProvider(value = stringsComp provides vmafCompleteDialogStrings) {
        SimpleDialog(
            title = strings[TITLE],
            content = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = strings[START_TIME])
                        Spacer(modifier = Modifier.width(width = dimens.xxs))
                        Text(text = startTime, style = MaterialTheme.typography.labelLarge)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = strings[END_TIME])
                        Spacer(modifier = Modifier.width(width = dimens.xxs))
                        Text(text = finishTime, style = MaterialTheme.typography.labelLarge)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = strings[DURATION_TIME])
                        Spacer(modifier = Modifier.width(width = dimens.xxs))
                        Text(text = duration, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(height = dimens.xs))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val color = MaterialTheme.colorScheme.tertiary

                        Text(
                            text = strings[SCORE],
                            style = MaterialTheme.typography.bodyLarge.copy(color = color)
                        )
                        Spacer(modifier = Modifier.width(width = dimens.xxs))
                        Text(text = score, style = MaterialTheme.typography.titleMedium.copy(color = color))
                    }
                }
            },
            icon = Icons.Rounded.CheckCircle,
            onConfirmation = onFinish,
            confirmationText = commonStrings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
private fun DefaultPreview() = VmafCompleteDialog(
    score = "12.345678",
    startTime = "123",
    finishTime = "123",
    duration = "123",
    onFinish = {}
)

@Preview
@Composable
private fun EnglishLight() = EnglishLightPreview { DefaultPreview() }

@Preview
@Composable
private fun EnglishDark() = EnglishDarkPreview { DefaultPreview() }

@Preview
@Composable
private fun PortugueseLight() = PortugueseLightPreview { DefaultPreview() }

@Preview
@Composable
private fun PortugueseDark() = PortugueseDarkPreview { DefaultPreview() }
