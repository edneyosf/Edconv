package edneyosf.edconv.features.home.states

data class HomeState(
    val navigation: HomeNavigationState = HomeNavigationState.Initial,
    val dialog: HomeDialogState = HomeDialogState.None,
    val loading: Boolean = false,
    val latestVersion: String? = null,
    val donationUrl: String? = null,
    val releasesUrl: String? = null
)