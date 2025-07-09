package edneyosf.edconv.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.app.ClosingDialogStrings.Keys.*
import edneyosf.edconv.ui.compositions.language

inline val closingDialogStrings: ClosingDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = ClosingDialogStrings(language)

class ClosingDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE_CLOSING_DIALOG to "Confirmar Saída",
        DESCRIPTION_CLOSING_DIALOG to "Tem certeza de que deseja sair?"
    )

    override val en = mapOf(
        TITLE_CLOSING_DIALOG to "Confirm Exit",
        DESCRIPTION_CLOSING_DIALOG to "Are you sure you want to exit?"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE_CLOSING_DIALOG,
        DESCRIPTION_CLOSING_DIALOG
    }
}