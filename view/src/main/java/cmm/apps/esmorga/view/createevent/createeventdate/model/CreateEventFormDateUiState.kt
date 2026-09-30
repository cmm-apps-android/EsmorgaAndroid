package cmm.apps.esmorga.view.createevent.createeventdate.model

data class CreateEventFormDateUiState(
    val selectedDateMillis: Long? = null,
    val selectedDeadlineDateMillis: Long? = null,
    val eventTime: String = "",
    val deadlineTime: String = "",
    val isButtonEnabled: Boolean = false,
    val isDeadlineToggleOn: Boolean = false,
    val deadlineErrorRes: Int? = null
)

sealed class CreateEventFormDateEffect {
    data object NavigateNext : CreateEventFormDateEffect()
    data object NavigateBack : CreateEventFormDateEffect()
}
