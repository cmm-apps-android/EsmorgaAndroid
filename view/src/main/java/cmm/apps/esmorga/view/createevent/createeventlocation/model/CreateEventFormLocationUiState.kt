package cmm.apps.esmorga.view.createevent.createeventlocation.model

data class CreateEventFormLocationUiState(
    val localizationName: String = "",
    val localizationCoordinates: String = "",
    val eventMaxCapacity: String = "",
    val isButtonEnabled: Boolean = false,
    val locationError: Int? = null,
    val coordinatesError: Int? = null,
    val capacityError: Int? = null,
)

sealed class CreateEventFormLocationEffect{
    data object NavigateNext : CreateEventFormLocationEffect()
    data object NavigateBack : CreateEventFormLocationEffect()
}