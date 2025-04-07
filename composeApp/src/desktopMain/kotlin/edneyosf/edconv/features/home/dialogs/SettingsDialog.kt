package edneyosf.edconv.features.home.dialogs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import edneyosf.edconv.core.Languages
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.features.home.texts.SettingsDialogTexts.Companion.NO_DEFINED_TXT
import edneyosf.edconv.features.home.texts.SettingsDialogTexts.Companion.SELECT_FFMPEG
import edneyosf.edconv.features.home.texts.SettingsDialogTexts.Companion.SELECT_FFPROBE
import edneyosf.edconv.features.home.texts.SettingsDialogTexts.Companion.TITLE_TXT
import edneyosf.edconv.features.home.texts.settingsDialogTexts
import edneyosf.edconv.ui.components.SimpleDialog
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.languagesComp
import edneyosf.edconv.ui.compositions.texts
import edneyosf.edconv.ui.compositions.textsComp
import edneyosf.edconv.ui.theme.AppTheme

@Composable
fun SettingsDialog(
    ffmpegPathDefault: String, ffprobePathDefault: String,
    onConfirmation: (ffmpegPath: String, ffprobe: String) -> Unit) {

    var ffmpegPath by remember { mutableStateOf(ffmpegPathDefault) }
    var ffprobePath by remember { mutableStateOf(ffprobePathDefault) }
    val noDefined = ffmpegPath.isBlank() || ffprobePath.isBlank()

    CompositionLocalProvider(textsComp provides settingsDialogTexts) {
        SimpleDialog(
            icon = Icons.Rounded.Settings,
            title = texts.get(TITLE_TXT),
            content = {
                Column {
                    if(ffmpegPath.isNotBlank()){
                        Text("FFmpeg: "+ffmpegPath)
                    }
                    if(ffprobePath.isNotBlank()){
                        Text("FFprobe: "+ffprobePath)
                    }
                    if(ffmpegPath.isBlank() && ffprobePath.isBlank()) {
                        Text(texts.get(NO_DEFINED_TXT))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(dimens.f)){
                        Button(
                            onClick = {
                                FileUtils.pickFile("Selecionar FFmpeg")?.let { ffmpegPath = it }
                            }
                        ){
                            Text(texts.get(SELECT_FFMPEG))
                        }
                        Button(
                            onClick = {
                                FileUtils.pickFile("Selecionar FFprobe")?.let { ffprobePath = it }
                            }
                        ){
                            Text(texts.get(SELECT_FFPROBE))
                        }
                    }
                }
            },
            confirmationButtonEnabled = !noDefined,
            onConfirmation = { onConfirmation(ffmpegPath, ffmpegPath) },
            onDismissRequest = { }
        )
    }
}

@Composable
private fun SettingsDialogPreview(
    ffmpegPath: String = "", ffprobePath: String = "", language: String, darkTheme: Boolean) {

    CompositionLocalProvider(languagesComp provides language) {
        AppTheme(darkTheme = darkTheme) {
            SettingsDialog(
                ffmpegPathDefault = ffmpegPath,
                ffprobePathDefault = ffprobePath,
                onConfirmation = { _,_ -> }
            )
        }
    }
}

// Default

@Preview
@Composable
private fun EnglishLight() = SettingsDialogPreview(language = Languages.EN, darkTheme = false)
@Preview
@Composable
private fun EnglishDark() = SettingsDialogPreview(language = Languages.EN, darkTheme = true)
@Preview
@Composable
private fun PortugueseLight() = SettingsDialogPreview(language = Languages.PT, darkTheme = false)
@Preview
@Composable
private fun PortugueseDark() = SettingsDialogPreview(language = Languages.PT, darkTheme = true)

// With FFmpeg path

@Preview
@Composable
private fun EnglishLightFFmpeg() = SettingsDialogPreview(
    ffmpegPath = "/dir/ffmpeg", language = Languages.EN, darkTheme = false)
@Preview
@Composable
private fun EnglishDarkFFmpeg() = SettingsDialogPreview(
    ffmpegPath = "/dir/ffmpeg", language = Languages.EN, darkTheme = true)
@Preview
@Composable
private fun PortugueseLightFFmpeg() = SettingsDialogPreview(
    ffmpegPath = "/dir/ffmpeg", language = Languages.PT, darkTheme = false)
@Preview
@Composable
private fun PortugueseDarkFFmpeg() = SettingsDialogPreview(
    ffmpegPath = "/dir/ffmpeg", language = Languages.PT, darkTheme = true)

// With FFprobe path

@Preview
@Composable
private fun EnglishLightFFprobe() = SettingsDialogPreview(
    ffprobePath = "/dir/ffprobe", language = Languages.EN, darkTheme = false)
@Preview
@Composable
private fun EnglishDarkFFprobe() = SettingsDialogPreview(
    ffprobePath = "/dir/ffprobe", language = Languages.EN, darkTheme = true)
@Preview
@Composable
private fun PortugueseLightFFprobe() = SettingsDialogPreview(
    ffprobePath = "/dir/ffprobe", language = Languages.PT, darkTheme = false)
@Preview
@Composable
private fun PortugueseDarkFFprobe() = SettingsDialogPreview(
    ffprobePath = "/dir/ffprobe", language = Languages.PT, darkTheme = true)

// With FFmpeg and FFprobe path

@Preview
@Composable
private fun EnglishLightFFmpegFFprobe() = SettingsDialogPreview(
    ffmpegPath = "/dir/ffmpeg", ffprobePath = "/dir/ffprobe", language = Languages.EN, darkTheme = false)
@Preview
@Composable
private fun EnglishDarkFFmpegFFprobe() = SettingsDialogPreview(
    ffmpegPath = "/dir/ffmpeg", ffprobePath = "/dir/ffprobe", language = Languages.EN, darkTheme = true)
@Preview
@Composable
private fun PortugueseLightFFmpegFFprobe() = SettingsDialogPreview(
    ffmpegPath = "/dir/ffmpeg", ffprobePath = "/dir/ffprobe", language = Languages.PT, darkTheme = false)
@Preview
@Composable
private fun PortugueseDarkFFmpegFFprobe() = SettingsDialogPreview(
    ffmpegPath = "/dir/ffmpeg", ffprobePath = "/dir/ffprobe", language = Languages.PT, darkTheme = true)