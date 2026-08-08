package edneyosf.edconv.feature.home

import androidx.compose.ui.draganddrop.DragAndDropEvent
import edneyosf.edconv.feature.home.states.HomeDialogState
import edneyosf.edconv.feature.home.states.HomeNavigationState

interface HomeAction {
    fun setNavigation(state: HomeNavigationState) = Unit
    fun setDialog(state: HomeDialogState) = Unit
    fun onDragAndDropInput(event: DragAndDropEvent) = false
    fun setInputs(paths: List<String>) = Unit
    fun openLink(url: String) = Unit
}