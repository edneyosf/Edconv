package edneyosf.edconv.core.extensions

import androidx.compose.runtime.MutableState

inline fun <T> MutableState<T>.update(block: T.() -> T) { value = value.block() }