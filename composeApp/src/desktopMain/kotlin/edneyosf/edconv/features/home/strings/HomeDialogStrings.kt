package edneyosf.edconv.features.home.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.home.strings.HomeDialogStrings.Keys.*

inline val homeDialogStrings: HomeDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = HomeDialogStrings(language)

class HomeDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        ERROR_TITLE to "Algo deu errado",
        WARNING_TITLE to "Atenção",
        COMPLETE_TITLE to "Conversão Concluída",
        CONFIRMATION_BUTTON to "Confirmar",
        CANCEL_BUTTON to "Cancelar",
        OVERWRITE_FILE to "O arquivo existe, deseja sobrescrever?",
        START_TIME to "Horário de Início:",
        END_TIME to "Horário de Término:",
        DURATION_TIME to "Duração total:",
    )

    override val en = mapOf(
        ERROR_TITLE to "Something went wrong",
        WARNING_TITLE to "Warning",
        COMPLETE_TITLE to "Conversion Completed",
        CONFIRMATION_BUTTON to "Confirm",
        CANCEL_BUTTON to "Cancel",
        OVERWRITE_FILE to "The file already exists. Do you want to overwrite it?",
        START_TIME to "Start time:",
        END_TIME to "End time:",
        DURATION_TIME to "Total duration:",
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        ERROR_TITLE,
        WARNING_TITLE,
        COMPLETE_TITLE,
        CONFIRMATION_BUTTON,
        CANCEL_BUTTON,
        OVERWRITE_FILE,
        START_TIME,
        END_TIME,
        DURATION_TIME
    }
}