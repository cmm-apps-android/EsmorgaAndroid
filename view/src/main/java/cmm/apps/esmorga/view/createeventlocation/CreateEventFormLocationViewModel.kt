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

	fun onBackClick() {
		_effect.tryEmit(CreateEventFormLocationEffect.NavigateBack)
	}

	fun onLocationChanged(text: String) {
		_uiState.update {
			val error = if (text.isBlank()) R.string.inline_error_location_required else null
			val isValid = !text.isBlank() && text.length <= 100
			it.copy(
				localizationName = text,
				locationError = error,
				isButtonEnabled = isValid,
				isFormValid = isValid
			)
		}
	}

	fun onCoordinatesChanged(text: String) {
		_uiState.update {
			it.copy(localizationCoordinates = text)
		}
	}

	fun onMaxCapacityChanged(text: String) {
		_uiState.update {
			it.copy(eventMaxCapacity = text)
		}
	}

	fun onNextClick() {
		val state = _uiState.value

		if (state.localizationName.isBlank()) {
			_uiState.update {
				it.copy(
					locationError = R.string.inline_error_location_required,
					isButtonEnabled = false,
					isFormValid = false
				)
			}
			return
		}

		val coordinates = state.localizationCoordinates.split(",").map { it.trim().toDoubleOrNull() }
		val lat = coordinates.getOrNull(0)
		val long = coordinates.getOrNull(1)

		val location = EventLocation(
			name = state.localizationName,
			lat = lat,
			long = long
		)

		val updatedForm = eventForm.copy(location = location)

		_effect.tryEmit(CreateEventFormLocationEffect.NavigateNext(updatedForm))
	}
}
