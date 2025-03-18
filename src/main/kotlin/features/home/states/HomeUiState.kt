package features.home.states

sealed interface HomeUiState {
    data object Initial: HomeUiState
    data object Loading: HomeUiState
    data class  Progress(val percentage: Float): HomeUiState
    data object Complete: HomeUiState
    data class Error(val message: String = ""): HomeUiState
}