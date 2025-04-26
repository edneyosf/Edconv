package edneyosf.edconv.features.home.states

sealed interface HomeNavigationState {
    data object Initial: HomeNavigationState
    data object Audio: HomeNavigationState
    data object Video: HomeNavigationState
    data object VMAF: HomeNavigationState
}