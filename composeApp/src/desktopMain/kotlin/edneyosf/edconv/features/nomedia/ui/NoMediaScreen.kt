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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edneyosf.edconv.core.utils.PropertyUtils
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
fun NoMediaScreen() {
    CompositionLocalProvider(value = stringsComp provides noMediaScreenStrings) {
        Content()
    }
}

@Composable
private fun Content() {
    val version = remember { PropertyUtils.version }

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
        if(version != null) Text(text = "${strings[VERSION]} $version")
    }
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides noMediaScreenStrings) {
        Content()
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
