package edneyosf.edconv.features.home.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language

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

    companion object Key {
        const val ERROR_TITLE = 1L
        const val WARNING_TITLE = 2L
        const val COMPLETE_TITLE = 3L
        const val CONFIRMATION_BUTTON = 4L
        const val CANCEL_BUTTON = 5L
        const val OVERWRITE_FILE = 6L
        const val START_TIME = 7L
        const val END_TIME = 8L
        const val DURATION_TIME = 9L
    }
}