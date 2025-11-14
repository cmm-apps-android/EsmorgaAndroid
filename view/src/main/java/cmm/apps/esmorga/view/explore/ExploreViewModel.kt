package cmm.apps.esmorga.view.explore

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.GetEventsAndPollsUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.view.explore.mapper.ExploreUiMapper.eventListToCardUiList
import cmm.apps.esmorga.view.explore.mapper.ExploreUiMapper.pollListToCardUiList
import cmm.apps.esmorga.view.explore.model.ExploreEffect
import cmm.apps.esmorga.view.explore.model.EventListUiState
import cmm.apps.esmorga.view.explore.model.ListCardUiModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val getEventListUseCase: GetEventsAndPollsUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<ExploreEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<ExploreEffect> = _effect.asSharedFlow()

    private var events: List<Event> = emptyList()
    private var polls: List<Poll> = emptyList()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        loadEventsAndPolls()
    }

    fun loadEventsAndPolls() {
        _uiState.value = EventListUiState(loading = true)
        viewModelScope.launch {
            val result = getEventListUseCase()
            result.onSuccess { success ->
                events = success.first
                polls = success.second
                _uiState.value = EventListUiState(
                    eventList = events.eventListToCardUiList(),
                    pollList = polls.pollListToCardUiList()
                )
            }.onFailure { error ->
                _uiState.value = EventListUiState(error = "${error.source} error: ${error.message}")
            }.onNoConnectionError {
                _effect.tryEmit(ExploreEffect.ShowNoNetworkPrompt)
            }
        }
    }

    fun onEventClick(event: ListCardUiModel) {
        events.find { event.id == it.id }?.let {
            _effect.tryEmit(ExploreEffect.NavigateToEventDetail(it))
        }
    }

    fun onPollClick(poll: ListCardUiModel) {
        polls.find { poll.id == it.id }?.let {
            _effect.tryEmit(ExploreEffect.NavigateToPollDetail(it))
        }
    }

}