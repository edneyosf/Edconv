package edneyosf.edconv.features.queue.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.queue.strings.QueueScreenStrings.Keys.*

inline val queueScreenStrings: QueueScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = QueueScreenStrings(language)

class QueueScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        WINDOW_TITLE to "Fila",
        CLEAN to "Limpar",
        SOURCE to "Origem",
        OUTPUT to "Destino",
        ERROR to "Erro",
        START_TIME to "Início",
        END_TIME to "Término",
        DURATION_TIME to "Duração",
        COMMAND to "Comando",
        EMPTY to "Fila vazia"
    )

    override val en = mapOf(
        WINDOW_TITLE to "Queue",
        CLEAN to "Clean",
        SOURCE to "Source",
        OUTPUT to "Output",
        ERROR to "Error",
        START_TIME to "Start",
        END_TIME to "End",
        DURATION_TIME to "Duration",
        COMMAND to "Command",
        EMPTY to "Empty queue"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        WINDOW_TITLE,
        CLEAN,
        SOURCE,
        OUTPUT,
        ERROR,
        START_TIME,
        END_TIME,
        DURATION_TIME,
        COMMAND,
        EMPTY
    }
}