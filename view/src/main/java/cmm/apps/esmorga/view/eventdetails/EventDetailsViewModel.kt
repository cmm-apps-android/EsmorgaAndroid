package cmm.apps.esmorga.view.eventdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
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
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val eventId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventDetailsUiState())
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventDetailsEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventDetailsEffect> = _effect.asSharedFlow()

    private var isAuthenticated: Boolean = false
    private var userJoined: Boolean = false

    init {
        viewModelScope.launch {
            val user = getSavedUserUseCase()
            val result = getEventDetailsUseCase(eventId)
            isAuthenticated = user.data != null
            result.onSuccess {
                userJoined = it.userJoined
                _uiState.value = it.toEventUiDetails(isAuthenticated, userJoined)
            }
        }
    }

    fun onNavigateClick() {
        _effect.tryEmit(
            EventDetailsEffect.NavigateToLocation(
                uiState.value.locationLat!!,
                uiState.value.locationLng!!
            )
        )
    }

    fun onBackPressed() {
        _effect.tryEmit(EventDetailsEffect.NavigateBack)
    }

    fun onPrimaryButtonClicked() {
        if (!isAuthenticated) {
            _effect.tryEmit(EventDetailsEffect.NavigateToLoginScreen)
        }
    }
}