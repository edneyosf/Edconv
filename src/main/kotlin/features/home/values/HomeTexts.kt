package features.home.values

import app.AppConfigs.LANGUAGE_DEFAULT
import app.AppLanguages.PT
import core.common.Texts

class HomeTexts(override val language: String = LANGUAGE_DEFAULT): Texts(language) {

    companion object {
        const val TITLE_PICK_FILE_TEXT = "title_pick_file"
    }

    override val texts = mapOf(
        PT to mapOf(TITLE_PICK_FILE_TEXT to "Escolha um arquivo")
    )
}