package edneyosf.edconv.features.settings.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Keys.*

inline val settingsDialogStrings: SettingsDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = SettingsDialogStrings(language)

class SettingsDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE to "Configuração",
        NO_DEFINED to "FFmpeg ou FFprobe não foram encontrados. Para continuar, indique os caminhos onde esses arquivos estão localizados.",
        DEFINED to "Informe os caminhos onde estão os arquivos do FFmpeg e do FFprobe.",
        SELECT_FFMPEG to "Selecionar FFmpeg",
        SELECT_FFPROBE to "Selecionar FFprobe",
        CONFIRMATION_BUTTON to "Confirmar",
        CANCEL_BUTTON to "Cancelar",
        PICK_FFMPEG_TITLE to "Selecione o arquivo FFmpeg",
        PICK_FFPROBE_TITLE to "Selecione o arquivo FFprobe",
        FFMPEG_OR_FFPROBE_VERIFICATION to "Falha na verificação de existência do FFmpeg/FFprobe.",
        CONFIGURATION_SAVE to "Falha ao salvar configuração."
    )

    override val en = mapOf(
        TITLE to "Settings",
        NO_DEFINED to "FFmpeg or FFprobe were not found. To continue, please specify the paths where these files are located.",
        DEFINED to "Please provide the paths to the FFmpeg and FFprobe files.",
        SELECT_FFMPEG to "Select FFmpeg",
        SELECT_FFPROBE to "Select FFprobe",
        CONFIRMATION_BUTTON to "Confirm",
        CANCEL_BUTTON to "Cancel",
        PICK_FFMPEG_TITLE to "Select FFmpeg file",
        PICK_FFPROBE_TITLE to "Select FFprobe file",
        FFMPEG_OR_FFPROBE_VERIFICATION to "Failed to verify the existence of FFmpeg/FFprobe.",
        CONFIGURATION_SAVE to "Failed to save configuration."
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE,
        NO_DEFINED,
        DEFINED,
        SELECT_FFMPEG,
        SELECT_FFPROBE,
        CONFIRMATION_BUTTON,
        CANCEL_BUTTON,
        PICK_FFMPEG_TITLE,
        PICK_FFPROBE_TITLE,
        FFMPEG_OR_FFPROBE_VERIFICATION,
        CONFIGURATION_SAVE
    }
}