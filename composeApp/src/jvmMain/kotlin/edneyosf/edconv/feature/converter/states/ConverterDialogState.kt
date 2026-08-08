package edneyosf.edconv.feature.converter.states

import edneyosf.edconv.feature.converter.enums.ConverterFileExistsAction

sealed interface ConverterDialogState {
    data object None: ConverterDialogState
    data object Settings: ConverterDialogState
    data class FileExists(val action: ConverterFileExistsAction): ConverterDialogState
}