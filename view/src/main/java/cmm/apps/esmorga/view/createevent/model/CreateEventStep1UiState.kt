package cmm.apps.esmorga.view.createevent.model

data class CreateEventStep1UiState(
    val eventName: String = "",
    val description: String = "",
    val eventNameError: Int? = null,
    val descriptionError: Int? = null,
    val isFormValid: Boolean = false
)

sealed class CreateEventStep1Effect {
    data object NavigateToStep2 : CreateEventStep1Effect()
    data object NavigateBack : CreateEventStep1Effect()
}