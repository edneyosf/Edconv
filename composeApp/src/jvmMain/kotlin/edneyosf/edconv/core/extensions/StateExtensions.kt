package edneyosf.edconv.core.extensions

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface Mutation<State> {
    fun reduce(state: State): State
}

@Deprecated("Use reduce instead", ReplaceWith("value = value.block()"))
inline fun <T> MutableState<T>.update(block: T.() -> T) { value = value.block() }

@Deprecated("Use reduce instead", ReplaceWith("value = value.block()"))
inline fun <T> MutableStateFlow<T>.update(block: T.() -> T) { value = value.block() }

fun <State> MutableStateFlow<State>.mutate(mutation: Mutation<State>) {
    update { currentState -> mutation.reduce(currentState) }
}