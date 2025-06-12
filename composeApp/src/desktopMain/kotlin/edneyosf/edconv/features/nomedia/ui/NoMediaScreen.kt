package edneyosf.edconv.features.nomedia.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.common.CommonStrings.Keys.VERSION
import edneyosf.edconv.features.nomedia.strings.NoMediaScreenStrings.Keys.*
import edneyosf.edconv.features.nomedia.strings.noMediaScreenStrings
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun NoMediaScreen(appVersion: String?) {
    CompositionLocalProvider(value = stringsComp provides noMediaScreenStrings) {
        Content(appVersion)
    }
}

@Composable
private fun Content(appVersion: String?) {
    Column(
        modifier = Modifier.fillMaxSize().padding(all = dimens.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(weight = 1f))
        Icon(
            modifier = Modifier.size(size = dimens.giga),
            imageVector = Icons.Rounded.VideoLibrary,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(height = dimens.xl))
        Text(text = strings[NO_FILE_SELECTED])
        Spacer(modifier = Modifier.weight(weight = 1f))
        if(appVersion != null) Text(text = "${commonStrings[VERSION]} $appVersion")
    }
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides noMediaScreenStrings) {
        Content(appVersion = "1.0.0")
    }
}

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
