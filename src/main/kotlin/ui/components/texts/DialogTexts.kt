package ui.components.texts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import core.Languages.EN
import core.Languages.PT
import core.common.Texts
import ui.compositions.languages

inline val dialogTexts: DialogTexts
    @ReadOnlyComposable
    @Composable
    get() = DialogTexts(languages)

class DialogTexts(override val language: String): Texts(language) {

    override val pt = mapOf(
        CONFIRMATION_BUTTON_TXT to "Confirmar",
        CANCEL_BUTTON_TXT to "Cancelar",
    )

    override val en = mapOf(
        CONFIRMATION_BUTTON_TXT to "Confirm",
        CANCEL_BUTTON_TXT to "Cancel",
    )

    override val texts = mapOf(PT to pt, EN to en)

    companion object {
        const val CONFIRMATION_BUTTON_TXT = 1L
        const val CANCEL_BUTTON_TXT = 2L
    }
}