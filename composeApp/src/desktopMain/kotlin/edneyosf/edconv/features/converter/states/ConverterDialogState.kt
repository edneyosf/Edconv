package edneyosf.edconv.features.converter.states

import edneyosf.edconv.features.converter.enums.ConverterFileExistsAction

sealed interface ConverterDialogState {
    data object None: ConverterDialogState
    data object Settings: ConverterDialogState
    data class FileExists(val action: ConverterFileExistsAction): ConverterDialogState
}