package edneyosf.edconv.ui.previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import edneyosf.edconv.core.Languages
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.ui.compositions.languageComp
import edneyosf.edconv.ui.theme.AppTheme

@Composable
fun LightPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = language,
        darkTheme = false,
        content = content
    )
}

@Composable
fun DarkPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = language,
        darkTheme = true,
        content = content
    )
}

@Composable
fun EnglishLightPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = Languages.EN,
        darkTheme = false,
        content = content
    )
}

@Composable
fun EnglishDarkPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = Languages.EN,
        darkTheme = true,
        content = content
    )
}

@Composable
fun PortugueseLightPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = Languages.PT,
        darkTheme = false,
        content = content
    )
}

@Composable
fun PortugueseDarkPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = Languages.PT,
        darkTheme = true,
        content = content
    )
}

@Composable
private fun DefaultPreview(language: String, darkTheme: Boolean, content: @Composable (() -> Unit)) {
    CompositionLocalProvider(languageComp provides language) {
        AppTheme(darkTheme = darkTheme) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.padding(dimens.md)) { content() }
            }
        }
    }
}