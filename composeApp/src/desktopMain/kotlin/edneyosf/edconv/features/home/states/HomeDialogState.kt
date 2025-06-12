package edneyosf.edconv.features.home.states

import edneyosf.edconv.core.common.Error

sealed interface HomeDialogState {
    data object None: HomeDialogState
    data class Failure(val error: Error): HomeDialogState
    data object Settings: HomeDialogState
}