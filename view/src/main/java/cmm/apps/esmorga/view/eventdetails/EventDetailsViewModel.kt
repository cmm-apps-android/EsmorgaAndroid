package cmm.apps.esmorga.view.eventdetails

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.designsystem.Loading
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.view.eventdetails.mapper.EventDetailsUiMapper.toEventUiDetails
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val joinEventUseCase: JoinEventUseCase,
    private val leaveEventUseCase: LeaveEventUseCase,
    event: Event
) : ViewModel(), DefaultLifecycleObserver {

    //Copy of the event passed as parameter so it can be internally modified by user actions such as join or leave. Event in local and remote will be modified in other layers.
    private var internalEvent = event

    private var isAuthenticated: Boolean = false

    private val _uiState = MutableStateFlow(EventDetailsUiState())
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventDetailsEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventDetailsEffect> = _effect.asSharedFlow()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        getLoginStatus()
    }

    fun onNavigateClick() {
        _effect.tryEmit(
            EventDetailsEffect.NavigateToLocation(
                internalEvent.location.lat ?: 0.0,
                internalEvent.location.long ?: 0.0,
                internalEvent.location.name
            )
        )
    }

    fun onBackPressed() {
        _effect.tryEmit(EventDetailsEffect.NavigateBack)
    }

    fun onPrimaryButtonClicked() {
        if (isAuthenticated) {
            if (internalEvent.userJoined) leaveEvent() else joinEvent()
        } else {
            _effect.tryEmit(EventDetailsEffect.NavigateToLoginScreen)
        }
    }

    fun onViewAttendeesClicked() {
        _effect.tryEmit(EventDetailsEffect.NavigateToAttendeesScreen(internalEvent))
    }

    private fun getLoginStatus() {
        viewModelScope.launch {
            val user = getSavedUserUseCase()
            isAuthenticated = user.data != null
            _uiState.value = internalEvent.toEventUiDetails(isAuthenticated)
        }
    }

    private fun joinEvent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(primaryButtonState = Loading)
            joinEventUseCase(internalEvent).onSuccess {
                internalEvent = internalEvent.copy(userJoined = true, currentAttendeeCount = internalEvent.currentAttendeeCount + 1)
                updateUiState()
                _effect.tryEmit(EventDetailsEffect.ShowJoinEventSuccess)
            }.onFailure { error ->
                if (error.code == ErrorCodes.EVENT_FULL) {
                    internalEvent.maxCapacity?.let { maxCapacity ->
                        internalEvent = internalEvent.copy(currentAttendeeCount = maxCapacity)
                    }
                    updateUiState()

                    _effect.tryEmit(EventDetailsEffect.ShowEventFullError)
                } else {
                    showErrorScreen(error)
                }
            }
        }
    }

    private fun leaveEvent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(primaryButtonState = Loading)
            leaveEventUseCase(internalEvent).onSuccess {
                internalEvent = internalEvent.copy(userJoined = false, currentAttendeeCount = internalEvent.currentAttendeeCount - 1)
                updateUiState()
                _effect.tryEmit(EventDetailsEffect.ShowLeaveEventSuccess)
            }.onFailure {
                showErrorScreen(it)
            }
        }
    }

    private fun updateUiState() {
        _uiState.value = internalEvent.toEventUiDetails(isAuthenticated)
    }

    private fun showErrorScreen(error: EsmorgaException) {
        updateUiState()
        if (error.code == ErrorCodes.NO_CONNECTION) {
            _effect.tryEmit(EventDetailsEffect.ShowNoNetworkError())
        } else {
            _effect.tryEmit(EventDetailsEffect.ShowFullScreenError())
        }
    }
}