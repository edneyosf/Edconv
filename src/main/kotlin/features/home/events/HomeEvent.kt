package features.home.events

sealed interface HomeEvent {
    data object OnStart: HomeEvent
    data class SetState(): HomeEvent
}