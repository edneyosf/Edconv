package edneyosf.edconv.features.home.states

sealed interface HomeDialogState {
    data object None: HomeDialogState
    data class Error(val id: String): HomeDialogState
    data object Settings: HomeDialogState
}