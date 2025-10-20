package cmm.apps.esmorga.view.eventattendees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.view.eventattendees.model.EventAttendeesEffect
import cmm.apps.esmorga.view.eventattendees.model.EventAttendeesUiState
import cmm.apps.esmorga.view.eventdetails.mapper.EventDetailsUiMapper.toEventUiDetails
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper.getPrimaryButtonTitle
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
    private val event: Event
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventAttendeesUiState())
    val uiState: StateFlow<EventAttendeesUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventAttendeesEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventAttendeesEffect> = _effect.asSharedFlow()

    init {
        getEventAttendees()
    }

    fun onBackPressed() {
        _effect.tryEmit(EventAttendeesEffect.NavigateBack)
    }

    private fun getEventAttendees(){
        viewModelScope.launch {
            //TODO get user list and add number before it
            _uiState.value = EventAttendeesUiState(loading = false, nameList = listOf("1. A", "2. B", "3. C"))
        }
    }

}