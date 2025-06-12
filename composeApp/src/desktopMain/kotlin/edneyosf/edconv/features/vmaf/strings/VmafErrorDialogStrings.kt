package edneyosf.edconv.features.vmaf.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.vmaf.strings.VmafErrorDialogStrings.Keys.*

inline val vmafErrorDialogStrings: VmafErrorDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = VmafErrorDialogStrings(language)

class VmafErrorDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        ON_STARTING_VMAF_REQUIREMENTS to "Vídeo de análise e/ou modelo VMAF são inválidos.",
        ON_STOPPING_VMAF to "Falha ao interromper análise.",
        SCORE_NULL to "A pontuação é nula.",
        START_TIME_NULL to "O tempo de início é nulo.",
        INPUT_FILE_NOT_EXIST to "Não foi possível localizar um dos arquivos necessários: vídeo de entrada, vídeo de análise ou modelo VMAF.",
        INPUT_NOT_FILE to "Vídeo de entrada, vídeo de análise e modelo VMAF devem ser arquivos válidos.",
        VMAF_PROCESS_COMPLETED to "A análise não pôde ser realizada com sucesso.",
        PROCESS_NULL to "O processo de análise é nulo.",
        VMAF_PROCESS to "Falha na análise.",
        NO_VIDEO_INPUT_MEDIA to "Nenhum fluxo de vídeo encontrado.",
        VMAF_MODEL_SAVE to "Não foi possível salvar o caminho do arquivo do modelo VMAF."
    )

    override val en = mapOf(
        ON_STARTING_VMAF_REQUIREMENTS to "Analysis video and/or VMAF model are invalid.",
        ON_STOPPING_VMAF to "Failed to stop analysis.",
        SCORE_NULL to "The score is null.",
        START_TIME_NULL to "The start time is null.",
        INPUT_FILE_NOT_EXIST to "Could not locate one of the required files: input video, analysis video, or VMAF model.",
        INPUT_NOT_FILE to "Input video, analysis video, and VMAF model must be valid files.",
        VMAF_PROCESS_COMPLETED to "The analysis could not be completed successfully.",
        PROCESS_NULL to "The analysis process is null.",
        VMAF_PROCESS to "Analysis failed.",
        NO_VIDEO_INPUT_MEDIA to "No video stream found.",
        VMAF_MODEL_SAVE to "Could not save the file path of the VMAF model."
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        ON_STARTING_VMAF_REQUIREMENTS,
        ON_STOPPING_VMAF,
        START_TIME_NULL,
        SCORE_NULL,
        INPUT_FILE_NOT_EXIST,
        INPUT_NOT_FILE,
        VMAF_PROCESS_COMPLETED,
        PROCESS_NULL,
        VMAF_PROCESS,
        NO_VIDEO_INPUT_MEDIA,
        VMAF_MODEL_SAVE
    }
}