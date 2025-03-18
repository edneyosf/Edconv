package core.common

import kotlinx.coroutines.CoroutineScope

abstract class ViewModel(protected open val scope: CoroutineScope)