package edneyosf.edconv.features.converter.extensions

import androidx.compose.runtime.Composable
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.features.common.CommonStrings.Keys.ERROR_DEFAULT
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.CONVERSION_PROCESS
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.CONVERSION_PROCESS_COMPLETED
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.FILE_ALREADY_EXISTS
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.INPUT_FILE_NOT_EXIST
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.INPUT_NOT_FILE
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.ON_ADD_TO_QUEUE
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.ON_ADD_TO_QUEUE_REQUIREMENTS
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.ON_STOPPING_CONVERSION
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.PROCESS_NULL
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.START_TIME_NULL
import edneyosf.edconv.features.converter.strings.converterErrorDialogStrings

@Composable
fun Error.toConverterDescription() = when(this) {
    Error.ON_ADD_TO_QUEUE -> converterErrorDialogStrings[ON_ADD_TO_QUEUE]
    Error.ON_ADD_TO_QUEUE_REQUIREMENTS -> converterErrorDialogStrings[ON_ADD_TO_QUEUE_REQUIREMENTS]
    Error.ON_STOPPING_CONVERSION -> converterErrorDialogStrings[ON_STOPPING_CONVERSION]
    Error.START_TIME_NULL -> converterErrorDialogStrings[START_TIME_NULL]
    Error.INPUT_FILE_NOT_EXIST -> converterErrorDialogStrings[INPUT_FILE_NOT_EXIST]
    Error.INPUT_NOT_FILE -> converterErrorDialogStrings[INPUT_NOT_FILE]
    Error.CONVERSION_PROCESS_COMPLETED -> converterErrorDialogStrings[CONVERSION_PROCESS_COMPLETED]
    Error.PROCESS_NULL -> converterErrorDialogStrings[PROCESS_NULL]
    Error.CONVERSION_PROCESS -> converterErrorDialogStrings[CONVERSION_PROCESS]
    Error.FILE_ALREADY_EXISTS -> converterErrorDialogStrings[FILE_ALREADY_EXISTS]
    else -> commonStrings[ERROR_DEFAULT]
}