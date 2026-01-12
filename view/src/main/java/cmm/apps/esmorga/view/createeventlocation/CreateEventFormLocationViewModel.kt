package cmm.apps.esmorga.view.createeventlocation

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.view.createeventlocation.model.CreateEventFormLocationEffect
import cmm.apps.esmorga.view.createeventlocation.model.CreateEventFormLocationUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

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
		val isValid = text.trim().length in 1..100

		_uiState.value = _uiState.value.copy(
			localizationName = text,
			isButtonEnabled = isValid
		)
	}

	fun onCoordinatesChanged(text: String) {
		_uiState.value = _uiState.value.copy(
			localizationCoordinates = text
		)
	}

	fun onMaxCapacityChanged(text: String) {
		_uiState.value = _uiState.value.copy(
			eventMaxCapacity = text
		)
	}

	fun onNextClick() {
		val coordinatess = _uiState.value.localizationCoordinates.split(",").map { it.trim().toDoubleOrNull() }

		val lat = coordinatess.getOrNull(0)
		val long = coordinatess.getOrNull(1)

		val location = EventLocation(
			name = _uiState.value.localizationName,
			lat = lat,
			long = long
		)

		val updatedForm = eventForm.copy(
			location = location
		)

		_effect.tryEmit(
			CreateEventFormLocationEffect.NavigateNext(updatedForm)
		)
	}


}
