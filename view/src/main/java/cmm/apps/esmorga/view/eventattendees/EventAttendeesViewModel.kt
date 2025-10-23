package cmm.apps.esmorga.view.eventattendees

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.GetEventAttendeesUseCase
import cmm.apps.esmorga.domain.event.model.EventAttendee
import cmm.apps.esmorga.view.eventattendees.model.EventAttendeesEffect
import cmm.apps.esmorga.view.eventattendees.model.EventAttendeesUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventAttendeesViewModel(
    private val getEventAttendeesUseCase: GetEventAttendeesUseCase,
    private val eventId: String
) : ViewModel(), DefaultLifecycleObserver {
    private val _uiState = MutableStateFlow(EventAttendeesUiState())
    val uiState: StateFlow<EventAttendeesUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventAttendeesEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventAttendeesEffect> = _effect.asSharedFlow()

    private var attendees: List<EventAttendee> = emptyList()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        getEventAttendees()
    }

    fun onBackPressed() {
        _effect.tryEmit(EventAttendeesEffect.NavigateBack)
    }

    private fun getEventAttendees() {
        _uiState.value = EventAttendeesUiState(loading = true)
        viewModelScope.launch {
            val result = getEventAttendeesUseCase(eventId)
            result.onSuccess { success ->
                attendees = success
                _uiState.value = EventAttendeesUiState(
                    nameList = success.mapIndexed { pos, attendee -> "${pos + 1}. ${attendee.name}" },
                )
            }.onFailure { error ->
                _effect.tryEmit(EventAttendeesEffect.ShowFullScreenError())
            }.onNoConnectionError {
                _effect.tryEmit(EventAttendeesEffect.ShowNoNetworkError())
            }
        }
    }

}