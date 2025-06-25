package edneyosf.edconv.features.queue.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.queue.strings.QueueItemStrings.Keys.*

inline val queueItemStrings: QueueItemStrings
    @ReadOnlyComposable
    @Composable
    get() = QueueItemStrings(language)

class QueueItemStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        AUDIO to "Áudio",
        VIDEO to "Vídeo",
        SUBTITLE to "Legenda",
        NOT_STARTED_STATUS to "Não iniciado",
        STARTED_STATUS to "Iniciado",
        IN_PROGRESS_STATUS to "Em andamento",
        FINISHED_STATUS to "Finalizado",
        ERROR_STATUS to "Erro",
        REMOVE to "Remover"
    )

    override val en = mapOf(
        AUDIO to "Audio",
        VIDEO to "Video",
        SUBTITLE to "Subtitle",
        NOT_STARTED_STATUS to "Not started",
        STARTED_STATUS to "Started",
        IN_PROGRESS_STATUS to "In progress",
        FINISHED_STATUS to "Finished",
        ERROR_STATUS to "Error",
        REMOVE to "Remove"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        AUDIO,
        VIDEO,
        SUBTITLE,
        NOT_STARTED_STATUS,
        STARTED_STATUS,
        IN_PROGRESS_STATUS,
        FINISHED_STATUS,
        ERROR_STATUS,
        REMOVE
    }
}