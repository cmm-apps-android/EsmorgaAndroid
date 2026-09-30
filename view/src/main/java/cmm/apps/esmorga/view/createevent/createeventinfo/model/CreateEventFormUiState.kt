package cmm.apps.esmorga.view.createevent.createeventinfo.model

data class CreateEventFormUiState(
    val eventName: String,
    val eventDescription: String? = null,
    val eventNameError: Int? = null,
    val descriptionError: Int? = null,
    val isFormValid: Boolean = false
)

sealed class CreateEventFormEffect {
    data object NavigateNext : CreateEventFormEffect()
    data object NavigateBack : CreateEventFormEffect()
}
