package edneyosf.edconv.feature.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class Store<State, Mutation>(
    initialState: State,
    private val reduce: (State, Mutation) -> State
): ViewModel() {
    private val _state = MutableStateFlow(value = initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    protected fun dispatch(update: Mutation) = _state.update { reduce(it, update) }
}