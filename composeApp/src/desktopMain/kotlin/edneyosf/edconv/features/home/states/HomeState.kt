package edneyosf.edconv.features.home.states

import edneyosf.edconv.ffmpeg.data.InputMedia

data class HomeState(
    val navigation: HomeNavigationState = HomeNavigationState.Initial,
    val dialog: HomeDialogState = HomeDialogState.None,
    val loading: Boolean = false,
    val input: InputMedia? = null
)