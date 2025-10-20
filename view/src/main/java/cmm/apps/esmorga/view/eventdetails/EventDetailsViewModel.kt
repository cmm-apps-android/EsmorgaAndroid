package cmm.apps.esmorga.view.eventdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
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

class EventDetailsViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val joinEventUseCase: JoinEventUseCase,
    private val leaveEventUseCase: LeaveEventUseCase,
    private val event: Event
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventDetailsUiState())
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventDetailsEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventDetailsEffect> = _effect.asSharedFlow()

    private var isAuthenticated: Boolean = false
    private var userJoined: Boolean = false

    private var isEventFull: Boolean = event.maxCapacity?.let { max -> event.currentAttendeeCount >= max } ?: false
    private var eventAttendeeCount = event.currentAttendeeCount

    init {
        getEventDetails()
    }

    fun onNavigateClick() {
        _effect.tryEmit(
            EventDetailsEffect.NavigateToLocation(
                uiState.value.locationLat ?: 0.0,
                uiState.value.locationLng ?: 0.0,
                uiState.value.locationName
            )
        )
    }

    fun onBackPressed() {
        _effect.tryEmit(EventDetailsEffect.NavigateBack)
    }

    fun onPrimaryButtonClicked() {
        if (isAuthenticated) {
            if (userJoined) {
                leaveEvent()
            } else {
                joinEvent()
            }
        } else {
            _effect.tryEmit(EventDetailsEffect.NavigateToLoginScreen)
        }
    }

    fun onViewAttendeesClicked() {
        _effect.tryEmit(EventDetailsEffect.NavigateToAttendeesScreen(event))
    }

    private fun getEventDetails() {
        viewModelScope.launch {
            val user = getSavedUserUseCase()
            isAuthenticated = user.data != null
            userJoined = event.userJoined
            _uiState.value = event.toEventUiDetails(isAuthenticated, userJoined, isEventFull)
        }
    }

    private fun joinEvent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(primaryButtonLoading = true)
            val result = joinEventUseCase(event)
            result.onSuccess {
                userJoined = true
                eventAttendeeCount = eventAttendeeCount + 1
                isEventFull = event.maxCapacity?.let { eventAttendeeCount >= it } ?: false
                _uiState.value = _uiState.value.copy(primaryButtonLoading = false, primaryButtonTitle = getPrimaryButtonTitle(isAuthenticated = true, userJoined = true, isEventFull), currentAttendeeCount = eventAttendeeCount)
                _effect.tryEmit(EventDetailsEffect.ShowJoinEventSuccess)
            }.onFailure { error ->
                showErrorScreen(error)
            }
        }
    }

    private fun leaveEvent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(primaryButtonLoading = true)
            val result = leaveEventUseCase(event)
            result.onSuccess {
                userJoined = false
                eventAttendeeCount = eventAttendeeCount - 1
                isEventFull = event.maxCapacity?.let { eventAttendeeCount >= it } ?: false
                _uiState.value =
                    _uiState.value.copy(primaryButtonLoading = false, primaryButtonTitle = getPrimaryButtonTitle(isAuthenticated = true, userJoined = false, isEventFull), isJoinButtonEnabled = !isEventFull, currentAttendeeCount = eventAttendeeCount)
                _effect.tryEmit(EventDetailsEffect.ShowLeaveEventSuccess)
            }.onFailure { error ->
                showErrorScreen(error)
            }
        }
    }

    private fun showErrorScreen(error: EsmorgaException) {
        _uiState.value = _uiState.value.copy(primaryButtonLoading = false)
        if (error.code == ErrorCodes.NO_CONNECTION) {
            _effect.tryEmit(EventDetailsEffect.ShowNoNetworkError())
        } else {
            _effect.tryEmit(EventDetailsEffect.ShowFullScreenError())
        }
    }

}