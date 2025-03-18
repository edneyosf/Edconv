package features.home.states

sealed interface HomeState {
    data object Initial: HomeState
    data object Loading: HomeState
    data class  Progress(val percentage: Float): HomeState
    data object Complete: HomeState
    data class Error(val message: String = ""): HomeState
}