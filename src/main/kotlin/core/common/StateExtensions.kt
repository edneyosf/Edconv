package core.common

import androidx.compose.runtime.MutableState

fun <T> MutableState<T>.update(block: T.() -> T) { value = value.block() }