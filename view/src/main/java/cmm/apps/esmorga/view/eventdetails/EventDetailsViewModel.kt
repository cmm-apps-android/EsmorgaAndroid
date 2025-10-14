package cmm.apps.esmorga.view.eventdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.mapper.EventDetailsUiMapper.toEventUiDetails
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper
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

    init {
        getEventDetails()
    }

    private fun updatePrimaryButton() {
        val maxCapacity = event.maxCapacity
        val currentCount = event.currentAttendeeCount
        val eventFull = maxCapacity != null && currentCount >= maxCapacity

        val buttonTitle = EventDetailsUiStateHelper.getPrimaryButtonTitle(
            isAuthenticated = isAuthenticated,
            userJoined = userJoined,
            eventFull = eventFull
        )

        _uiState.value = event.toEventUiDetails(isAuthenticated, userJoined, eventFull).copy(
            primaryButtonTitle = buttonTitle,
            isJoinButtonEnabled = isAuthenticated && (userJoined || !eventFull),
            isEventFull = eventFull
        )
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
        if (!isAuthenticated) {
            _effect.tryEmit(EventDetailsEffect.NavigateToLoginScreen)
            return
        }

        if (!_uiState.value.isJoinButtonEnabled && !userJoined) {
            _effect.tryEmit(
                EventDetailsEffect.ShowFullScreenError(
                    esmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()
                )
            )
            return
        }

        if (userJoined) leaveEvent() else joinEvent()
    }

    private fun getEventDetails() {
        viewModelScope.launch {
            val user = getSavedUserUseCase()
            isAuthenticated = user.data != null
            userJoined = event.userJoined
            updatePrimaryButton()
        }
    }

    private fun joinEvent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(primaryButtonLoading = true)
            val result = joinEventUseCase(event)
            result.onSuccess {
                userJoined = true
                updatePrimaryButton()
                _effect.tryEmit(EventDetailsEffect.ShowJoinEventSuccess)
            }.onFailure { error ->
                handleJoinLeaveError(error)
            }
        }
    }

    private fun leaveEvent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(primaryButtonLoading = true)
            val result = leaveEventUseCase(event)
            result.onSuccess {
                userJoined = false
                updatePrimaryButton()
                _effect.tryEmit(EventDetailsEffect.ShowLeaveEventSuccess)
            }.onFailure { error ->
                handleJoinLeaveError(error)
            }
        }
    }

    private fun handleJoinLeaveError(error: EsmorgaException) {
        _uiState.value = _uiState.value.copy(primaryButtonLoading = false)

        if (error.code == 422 ) {
            _effect.tryEmit(EventDetailsEffect.ShowFullScreenError())
            _uiState.value = _uiState.value.copy(
                currentAttendeeCount = event.currentAttendeeCount
            )
        } else {
            showErrorScreen(error)
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