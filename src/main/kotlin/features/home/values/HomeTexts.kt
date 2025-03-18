package features.home.values

import app.AppConfigs.LANGUAGE
import app.AppLanguages.PT
import core.common.Texts

class HomeTexts(override val language: String = LANGUAGE): Texts(language) {

    companion object {
        const val TITLE_PICK_FILE_TEXT = "title_pick_file"
        const val SELECT_FILE_TEXT = "select_file"
    }

    override val texts = mapOf(
        PT to mapOf(TITLE_PICK_FILE_TEXT to "Escolha um arquivo"),
        PT to mapOf(SELECT_FILE_TEXT to "Selecionar arquivo")
    )
}