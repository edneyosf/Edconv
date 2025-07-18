package edneyosf.edconv.features.home

import androidx.compose.ui.draganddrop.DragAndDropEvent
import edneyosf.edconv.features.home.states.HomeDialogState
import edneyosf.edconv.features.home.states.HomeNavigationState

interface HomeEvent {
    fun setNavigation(state: HomeNavigationState) = Unit
    fun setDialog(state: HomeDialogState) = Unit
    fun pickFile(title: String) = Unit
    fun onDragAndDropInput(event: DragAndDropEvent) = false
}