package edneyosf.edconv.features.home.events

import edneyosf.edconv.features.home.states.HomeDialogState
import edneyosf.edconv.features.home.states.HomeNavigationState

interface HomeEvent {
    fun setNavigation(state: HomeNavigationState) = Unit
    fun setDialog(state: HomeDialogState) = Unit
    fun pickFile(title: String) = Unit
}