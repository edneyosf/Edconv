package edneyosf.edconv.features.metrics.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.metrics.strings.MetricsErrorDialogStrings.Keys.*

inline val metricsErrorDialogStrings: MetricsErrorDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = MetricsErrorDialogStrings(language)

class MetricsErrorDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        ON_STARTING_METRICS_REQUIREMENTS to "Vídeo distorcido é inválido.",
        ON_STOPPING_METRICS to "Falha ao interromper análise.",
        SCORE_NULL to "A pontuação é nula.",
        START_TIME_NULL to "O tempo de início é nulo.",
        INPUT_FILE_NOT_EXIST to "Não foi possível localizar um dos arquivos necessários: vídeo de referência ou vídeo distorcido.",
        INPUT_NOT_FILE to "Vídeo de referência e vídeo de distorcido devem ser arquivos válidos.",
        METRICS_PROCESS_COMPLETED to "A análise não pôde ser realizada com sucesso.",
        PROCESS_NULL to "O processo de análise é nulo.",
        METRICS_PROCESS to "Falha na análise.",
        NO_VIDEO_INPUT_MEDIA to "Nenhum fluxo de vídeo encontrado."
    )

    override val en = mapOf(
        ON_STARTING_METRICS_REQUIREMENTS to "Distorted video is invalid.",
        ON_STOPPING_METRICS to "Failed to stop analysis.",
        SCORE_NULL to "The score is null.",
        START_TIME_NULL to "The start time is null.",
        INPUT_FILE_NOT_EXIST to "Could not locate one of the required files: reference video or distorted video.",
        INPUT_NOT_FILE to "Reference video and distorted video must be valid files.",
        METRICS_PROCESS_COMPLETED to "The analysis could not be completed successfully.",
        PROCESS_NULL to "The analysis process is null.",
        METRICS_PROCESS to "Analysis failed.",
        NO_VIDEO_INPUT_MEDIA to "No video stream found."
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        ON_STARTING_METRICS_REQUIREMENTS,
        ON_STOPPING_METRICS,
        START_TIME_NULL,
        SCORE_NULL,
        INPUT_FILE_NOT_EXIST,
        INPUT_NOT_FILE,
        METRICS_PROCESS_COMPLETED,
        PROCESS_NULL,
        METRICS_PROCESS,
        NO_VIDEO_INPUT_MEDIA
    }
}