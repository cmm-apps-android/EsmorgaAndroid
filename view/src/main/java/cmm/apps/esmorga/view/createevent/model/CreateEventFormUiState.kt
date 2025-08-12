package cmm.apps.esmorga.view.createevent.model

data class CreateEventFormUiState(
    val eventName: String = "",
    val description: String = "",
    val eventNameError: Int? = null,
    val descriptionError: Int? = null,
    val isFormValid: Boolean = false
)

sealed class CreateEventFormEffect {
    data object NavigateEventType : CreateEventFormEffect()
    data object NavigateBack : CreateEventFormEffect()
}