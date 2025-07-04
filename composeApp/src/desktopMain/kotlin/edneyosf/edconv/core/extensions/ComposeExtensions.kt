package edneyosf.edconv.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
inline fun <T> LaunchedEffected(key: T, crossinline block: suspend (T) -> Unit) {
    LaunchedEffect(key) {
        block(key)
    }
}