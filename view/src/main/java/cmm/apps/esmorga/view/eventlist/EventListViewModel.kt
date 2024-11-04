package cmm.apps.esmorga.view.eventlist

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.eventlist.mapper.EventListUiMapper.toEventUiList
import cmm.apps.esmorga.view.eventlist.model.EventListEffect
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.EventListUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventListViewModel(private val getEventListUseCase: GetEventListUseCase) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventListEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventListEffect> = _effect.asSharedFlow()

    private var events: List<Event> = emptyList()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        loadEvents()
    }

    fun loadEvents() {
        _uiState.value = EventListUiState(loading = true)
        viewModelScope.launch {
            val result = getEventListUseCase()
            result.onSuccess { success ->
                events = success
                _uiState.value = EventListUiState(
                    eventList = success.toEventUiList()
                )
            }.onFailure { error ->
                _uiState.value = EventListUiState(error = "${error.source} error: ${error.message}")
            }.onNoConnectionError {
                _effect.tryEmit(EventListEffect.ShowNoNetworkPrompt)
            }
        }
    }

    fun onEventClick(event: EventListUiModel) {
        val eventFound = events.find { event.id == it.id }
        eventFound?.let {
            _effect.tryEmit(EventListEffect.NavigateToEventDetail(it))
        }
    }

}