package cmm.apps.esmorga.view.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.view.eventlist.mapper.EventListUiMapper.toEventUiList
import cmm.apps.esmorga.view.eventlist.model.EventListEffect
import cmm.apps.esmorga.view.eventlist.model.EventListUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventListViewModel(private val getEventListUseCase: GetEventListUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventListEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventListEffect> = _effect.asSharedFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        _uiState.value = EventListUiState(loading = true)
        viewModelScope.launch {
            val result = getEventListUseCase()

            result.onSuccess { success ->
                if (success.hasError() && success.nonBlockingError == ErrorCodes.NO_CONNECTION) {
                    _effect.tryEmit(EventListEffect.ShowNoNetworkPrompt)
                }
                _uiState.value = EventListUiState(
                    eventList = success.data.toEventUiList(),
                    error = if (success.hasError() && success.nonBlockingError == ErrorCodes.NO_CONNECTION && success.data.isEmpty()) "No Connection" else null
                )
            }.onFailure { error ->
                if (error is EsmorgaException) {
                    _uiState.value = EventListUiState(error = "${error.source} error: ${error.message}")
                } else {
                    _uiState.value = EventListUiState(error = "Unknown error: ${error.message}")
                }
            }
        }
    }

    fun onEventClick(eventId: String) {
        _effect.tryEmit(EventListEffect.NavigateToEventDetail(eventId))
    }

}