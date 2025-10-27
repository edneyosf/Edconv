package edneyosf.edconv.features.home.states

import edneyosf.edconv.features.common.models.InputMedia

data class HomeState(
    val navigation: HomeNavigationState = HomeNavigationState.Initial,
    val dialog: HomeDialogState = HomeDialogState.None,
    val loading: Boolean = false,
    val input: InputMedia? = null,
    val latestVersion: String? = null,
    val donationUrl: String? = null,
    val releasesUrl: String? = null
)