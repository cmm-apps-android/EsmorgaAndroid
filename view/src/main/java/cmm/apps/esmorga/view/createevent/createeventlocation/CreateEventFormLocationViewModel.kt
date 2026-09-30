package cmm.apps.esmorga.view.createevent.createeventlocation

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.CreateEventFlowSession
import cmm.apps.esmorga.view.createevent.createeventlocation.model.CreateEventFormLocationEffect
import cmm.apps.esmorga.view.createevent.createeventlocation.model.CreateEventFormLocationUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateEventFormLocationViewModel(
    private val createEventFlowSession: CreateEventFlowSession
) : ViewModel() {

    companion object {
        const val LOCATION_NAME_MAX_LENGTH = 100
        const val MAX_CAPACITY_LIMIT = 5000
    }

    private val _uiState = MutableStateFlow(CreateEventFormLocationUiState())
    val uiState: StateFlow<CreateEventFormLocationUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormLocationEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventFormLocationEffect> = _effect.asSharedFlow()

    private val coordsRegex = Regex("^-?\\d+(\\.\\d+)?\\s*,\\s*-?\\d+(\\.\\d+)?$")

    init {
        restoreFromFlow()
    }

    private fun restoreFromFlow() {
        val form = createEventFlowSession.eventForm.value
        val location = form.location
        val coordinates = if (location?.lat != null && location.long != null) {
            "${location.lat}, ${location.long}"
        } else {
            ""
        }
        val currentState = CreateEventFormLocationUiState(
            localizationName = location?.name.orEmpty(),
            localizationCoordinates = coordinates,
            eventMaxCapacity = form.maxCapacity?.toString().orEmpty()
        )
        _uiState.value = currentState.withFormValidation()
    }

    fun onBackClick() {
        persistLocationSelection()
        _effect.tryEmit(CreateEventFormLocationEffect.NavigateBack)
    }

    fun onLocationChanged(text: String) {
        if (text.length > LOCATION_NAME_MAX_LENGTH) return

        updateFormState(
            _uiState.value.copy(
                localizationName = text,
                locationError = if (text.isBlank()) R.string.inline_error_location_required else null
            )
        )
    }

    fun onCoordinatesChanged(text: String) {
        val trimmedText = text.trim()
        val isInvalid = trimmedText.isNotEmpty() && !trimmedText.matches(coordsRegex)
        val error = if (isInvalid) R.string.inline_error_coordinates_invalid else null

        updateFormState(
            _uiState.value.copy(
                localizationCoordinates = text,
                coordinatesError = error
            )
        )
    }

    fun onMaxCapacityChanged(text: String) {
        if (!text.all { it.isDigit() }) return

        val capacityInt = text.toIntOrNull()
        val isInvalid = text.isNotBlank() && (capacityInt == null || capacityInt < 1 || capacityInt > MAX_CAPACITY_LIMIT)
        val error = if (isInvalid) R.string.inline_error_max_capacity_invalid else null

        updateFormState(
            _uiState.value.copy(
                eventMaxCapacity = text,
                capacityError = error
            )
        )
    }

    private fun validateForm(state: CreateEventFormLocationUiState): Boolean {
        val hasNoErrors = state.locationError == null &&
                state.coordinatesError == null &&
                state.capacityError == null

        return state.localizationName.isNotBlank() && hasNoErrors
    }

    private fun updateFormState(state: CreateEventFormLocationUiState) {
        _uiState.value = state.withFormValidation()
    }

    private fun CreateEventFormLocationUiState.withFormValidation(): CreateEventFormLocationUiState {
        return copy(isButtonEnabled = validateForm(this))
    }

    fun onNextClick() {
        val state = _uiState.value
        if (!validateForm(state)) return

        persistLocationSelection()
        _effect.tryEmit(CreateEventFormLocationEffect.NavigateNext)
    }

    private fun persistLocationSelection() {
        val state = _uiState.value
        val coordsParts = state.localizationCoordinates.split(",").map { it.trim().toDoubleOrNull() }
        val location = EventLocation(
            name = state.localizationName,
            lat = coordsParts.getOrNull(0),
            long = coordsParts.getOrNull(1)
        )

        createEventFlowSession.updateLocation(
            location = location,
            maxCapacity = state.eventMaxCapacity.toIntOrNull()
        )
    }
}