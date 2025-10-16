package edneyosf.edconv.features.home.states

sealed interface HomeNavigationState {
    data object Initial: HomeNavigationState
    data object Media: HomeNavigationState
    data object Metrics: HomeNavigationState
}