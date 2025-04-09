package edneyosf.edconv.ui.previews

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import edneyosf.edconv.core.Languages
import edneyosf.edconv.ui.compositions.languagesComp
import edneyosf.edconv.ui.theme.AppTheme

@Composable
private fun DefaultPreview(language: String, darkTheme: Boolean, content: @Composable (() -> Unit)) {
    CompositionLocalProvider(languagesComp provides language) {
        AppTheme(darkTheme = darkTheme) {
            Surface(modifier = Modifier.fillMaxSize(), content = content)
        }
    }
}

@Composable
fun EnglishLightPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = Languages.EN,
        darkTheme = false,
        content = content
    )
}
@Preview
@Composable
fun EnglishDarkPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = Languages.EN,
        darkTheme = true,
        content = content
    )
}
@Preview
@Composable
fun PortugueseLightPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = Languages.PT,
        darkTheme = false,
        content = content
    )
}

@Preview
@Composable
fun PortugueseDarkPreview(content: @Composable (() -> Unit)) {
    DefaultPreview(
        language = Languages.PT,
        darkTheme = true,
        content = content
    )
}