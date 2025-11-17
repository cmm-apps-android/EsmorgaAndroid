package cmm.apps.esmorga.view.eventattendees

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.GetEventAttendeesUseCase
import cmm.apps.esmorga.domain.event.UpdateEventAttendeeUseCase
import cmm.apps.esmorga.domain.event.model.EventAttendee
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.model.RoleType
import cmm.apps.esmorga.view.eventattendees.mapper.EventAttendeeUiMapper.toAttendeeUiList
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
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val getEventAttendeesUseCase: GetEventAttendeesUseCase,
    private val updateEventAttendeeUseCase: UpdateEventAttendeeUseCase,
    private val eventId: String
) : ViewModel(), DefaultLifecycleObserver {
    private val _uiState = MutableStateFlow(EventAttendeesUiState())
    val uiState: StateFlow<EventAttendeesUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventAttendeesEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventAttendeesEffect> = _effect.asSharedFlow()

    private val attendees: MutableList<EventAttendee> = mutableListOf()
    private var showChecked: Boolean = false

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        getEventAttendees()
    }

    fun onAttendeeChecked(position: Int, checked: Boolean) {
        val attendee = attendees[position]
        viewModelScope.launch {
            val updatedAttendee = attendee.copy(alreadyPaid = checked)
            updateEventAttendeeUseCase(updatedAttendee)
            attendees[position] = updatedAttendee
            updateUi()
        }
    }

    fun onBackPressed() {
        _effect.tryEmit(EventAttendeesEffect.NavigateBack)
    }

    private fun getEventAttendees() {
        _uiState.value = EventAttendeesUiState(loading = true)
        viewModelScope.launch {
            val userResult = getSavedUserUseCase()
            showChecked = RoleType.ADMIN == userResult.data?.role
            val eventAttendeesResult = getEventAttendeesUseCase(eventId)
            eventAttendeesResult.onSuccess { successData ->
                attendees.run {
                    clear()
                    addAll(successData)
                }
                updateUi()
            }.onFailure { error ->
                _effect.tryEmit(EventAttendeesEffect.ShowFullScreenError())
            }.onNoConnectionError {
                _effect.tryEmit(EventAttendeesEffect.ShowNoNetworkError())
            }
        }
    }

    private fun updateUi(){
        _uiState.value = EventAttendeesUiState(
            showChecked = showChecked,
            attendeeList = attendees.toAttendeeUiList(),
        )
    }

}