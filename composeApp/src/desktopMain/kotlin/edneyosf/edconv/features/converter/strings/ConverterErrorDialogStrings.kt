package edneyosf.edconv.features.converter.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.*

inline val converterErrorDialogStrings: ConverterErrorDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = ConverterErrorDialogStrings(language)

class ConverterErrorDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        ON_ADD_TO_QUEUE to "Não foi possível adicionar a mídia à fila de conversão.",
        ON_ADD_TO_QUEUE_REQUIREMENTS to "O comando de entrada e/ou o arquivo de saída são inválidos.",
        ON_STOPPING_CONVERSION to "Falha ao interromper conversão.",
        START_TIME_NULL to "O tempo de início é nulo.",
        INPUT_FILE_NOT_EXIST to "O arquivo de entrada não existe.",
        INPUT_NOT_FILE to "A mídia de entrada não é um arquivo.",
        CONVERSION_PROCESS_COMPLETED to "A conversão não pôde ser realizada com sucesso.",
        PROCESS_NULL to "O processo de conversão é nulo.",
        CONVERSION_PROCESS to "Falha na conversão."
    )

    override val en = mapOf(
        ON_ADD_TO_QUEUE to "Could not add the media to the conversion queue.",
        ON_ADD_TO_QUEUE_REQUIREMENTS to "Either the input command and/or the output file are invalid.",
        ON_STOPPING_CONVERSION to "Failed to stop conversion.",
        START_TIME_NULL to "The start time is null.",
        INPUT_FILE_NOT_EXIST to "The input file does not exist.",
        INPUT_NOT_FILE to "The input media is not a file.",
        CONVERSION_PROCESS_COMPLETED to "The conversion could not be completed successfully.",
        PROCESS_NULL to "The conversion process is null.",
        CONVERSION_PROCESS to "Conversion failed."
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        ON_ADD_TO_QUEUE,
        ON_ADD_TO_QUEUE_REQUIREMENTS,
        ON_STOPPING_CONVERSION,
        START_TIME_NULL,
        INPUT_FILE_NOT_EXIST,
        INPUT_NOT_FILE,
        CONVERSION_PROCESS_COMPLETED,
        PROCESS_NULL,
        CONVERSION_PROCESS
    }
}