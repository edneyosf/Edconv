package features.home.states.status

sealed interface HomeStatus {
    data object Initial: HomeStatus
    data object Loading: HomeStatus
    data class Progress(val percentage: Float): HomeStatus
    data object Complete: HomeStatus
    data class Error(val message: String = ""): HomeStatus
}