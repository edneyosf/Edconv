package features.home.texts

import app.AppConfigs.LANGUAGE
import app.AppLanguages.EN
import app.AppLanguages.PT
import core.common.Texts

class HomeTexts(override val language: String = LANGUAGE): Texts(language) {

    override val pt = mapOf(
        TITLE_PICK_FILE_TEXT to "Escolha um arquivo",
        SELECT_FILE_TEXT to "Selecionar arquivo"
    )

    override val en = mapOf(
        TITLE_PICK_FILE_TEXT to "Choose a file",
        SELECT_FILE_TEXT to "Select file"
    )

    override val texts = mapOf(PT to pt, EN to en)

    companion object {
        const val TITLE_PICK_FILE_TEXT = 1L
        const val SELECT_FILE_TEXT = 2L
    }
}