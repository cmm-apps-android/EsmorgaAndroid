package cmm.apps.esmorga.view.createeventlocation

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createeventlocation.model.CreateEventFormLocationEffect
import cmm.apps.esmorga.view.createeventlocation.model.CreateEventFormLocationUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateEventFormLocationViewModel(
	private val eventForm: CreateEventForm
) : ViewModel() {

	private val _uiState = MutableStateFlow(CreateEventFormLocationUiState())
	val uiState: StateFlow<CreateEventFormLocationUiState> = _uiState.asStateFlow()

	private val _effect = MutableSharedFlow<CreateEventFormLocationEffect>(
		extraBufferCapacity = 1,
		onBufferOverflow = BufferOverflow.DROP_OLDEST
	)
	val effect: SharedFlow<CreateEventFormLocationEffect> = _effect.asSharedFlow()

	private val coordsRegex = Regex("^-?\\d+(\\.\\d+)?\\s*,\\s*-?\\d+(\\.\\d+)?$")

	fun onBackClick() {
		_effect.tryEmit(CreateEventFormLocationEffect.NavigateBack)
	}

	fun onLocationChanged(text: String) {
		if (text.length <= 100) {
			_uiState.update { state ->
				val error = if (text.isBlank()) R.string.inline_error_location_required else null
				val newState = state.copy(localizationName = text, locationError = error)
				newState.copy(isButtonEnabled = validateForm(newState))
			}
		}
	}

	fun onCoordinatesChanged(text: String) {
		_uiState.update { state ->
			val isInvalid = text.isNotBlank() && !text.trim().matches(coordsRegex)
			val error = if (isInvalid) R.string.inline_error_coordinates_invalid else null

			val newState = state.copy(localizationCoordinates = text, coordinatesError = error)
			newState.copy(isButtonEnabled = validateForm(newState))
		}
	}

	fun onMaxCapacityChanged(text: String) {
		if (text.all { it.isDigit() }) {
			_uiState.update { state ->
				val capacityInt = text.toIntOrNull()
				val isInvalid = text.isNotBlank() && (capacityInt == null || capacityInt <= 0)
				val error = if (isInvalid) R.string.inline_error_max_capacity_invalid else null

				val newState = state.copy(eventMaxCapacity = text, capacityError = error)
				newState.copy(isButtonEnabled = validateForm(newState))
			}
		}
	}

	private fun validateForm(state: CreateEventFormLocationUiState): Boolean {
		val hasNoErrors = state.locationError == null &&
				state.coordinatesError == null &&
				state.capacityError == null

		return state.localizationName.isNotBlank() && hasNoErrors
	}

	fun onNextClick() {
		val state = _uiState.value
		if (!validateForm(state)) return

		val coordsParts = state.localizationCoordinates.split(",").map { it.trim().toDoubleOrNull() }
		val location = EventLocation(
			name = state.localizationName,
			lat = coordsParts.getOrNull(0),
			long = coordsParts.getOrNull(1)
		)

		val updatedForm = eventForm.copy(
			location = location,
			maxCapacity = state.eventMaxCapacity.toIntOrNull()
		)

		_effect.tryEmit(CreateEventFormLocationEffect.NavigateNext(updatedForm))
	}
}