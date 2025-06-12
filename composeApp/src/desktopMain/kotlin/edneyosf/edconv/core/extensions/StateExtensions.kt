package edneyosf.edconv.core.extensions

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow

inline fun <T> MutableState<T>.update(block: T.() -> T) { value = value.block() }

inline fun <T> MutableStateFlow<T>.update(block: T.() -> T) { value = value.block() }