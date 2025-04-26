package edneyosf.edconv.features.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.features.common.CommonStrings.Keys.*
import edneyosf.edconv.ui.compositions.language

inline val commonStrings: CommonStrings
    @ReadOnlyComposable
    @Composable
    get() = CommonStrings(language)

class CommonStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        ERROR_TITLE to "Opa! Algo deu errado",
        WARNING_TITLE to "Atenção",
        ERROR_DEFAULT to "Ocorreu um erro inesperado.",
        CONFIRMATION_BUTTON to "Confirmar",
        CANCEL_BUTTON to "Cancelar",
        VERSION to "Versão"
    )

    override val en = mapOf(
        ERROR_TITLE to "Oops! Something went wrong",
        WARNING_TITLE to "Warning",
        ERROR_DEFAULT to "An unexpected error occurred.",
        CONFIRMATION_BUTTON to "Confirm",
        CANCEL_BUTTON to "Cancel",
        VERSION to "Version"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        ERROR_TITLE,
        WARNING_TITLE,
        ERROR_DEFAULT,
        CONFIRMATION_BUTTON,
        CANCEL_BUTTON,
        VERSION
    }
}