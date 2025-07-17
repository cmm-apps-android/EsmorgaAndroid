package cmm.apps.esmorga.view.eventlist

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.GetMyEventListUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.user.GetIfUserIsAdminUsecase
import cmm.apps.esmorga.view.eventlist.mapper.EventListUiMapper.toEventUiList
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.MyEventListEffect
import cmm.apps.esmorga.view.eventlist.model.MyEventListError
import cmm.apps.esmorga.view.eventlist.model.MyEventListUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyEventListViewModel(
    private val getMyEventListUseCase: GetMyEventListUseCase,
    private val getIfUserIsAdminUsecase: GetIfUserIsAdminUsecase
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(MyEventListUiState())
    val uiState: StateFlow<MyEventListUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<MyEventListEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<MyEventListEffect> = _effect.asSharedFlow()

    private var events: List<Event> = emptyList()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        checkIfUserIsAdmin()
        loadMyEvents()
    }

    fun loadMyEvents() {
        _uiState.value = _uiState.value.copy(loading = true)

        viewModelScope.launch {
            val result = getMyEventListUseCase()

            result.onSuccess { myEventList ->
                events = myEventList
                _uiState.value = if (myEventList.isEmpty()) {
                    _uiState.value.copy(
                        loading = false,
                        error = MyEventListError.EMPTY_LIST
                    )
                } else {
                    _uiState.value.copy(
                        loading = false,
                        eventList = myEventList.toEventUiList(),
                        error = null
                    )
                }
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = when (error.code) {
                        ErrorCodes.NOT_LOGGED_IN -> MyEventListError.NOT_LOGGED_IN
                        else -> MyEventListError.UNKNOWN
                    }
                )
            }.onNoConnectionError {
                _effect.tryEmit(MyEventListEffect.ShowNoNetworkPrompt)
            }
        }
    }

    fun checkIfUserIsAdmin() {
        viewModelScope.launch {
            val isAdmin = getIfUserIsAdminUsecase()
            _uiState.value = _uiState.value.copy(isAdmin = isAdmin)
        }
    }


    fun onEventClick(event: EventListUiModel) {
        val eventFound = events.find { it.id == event.id }
        eventFound?.let {
            _effect.tryEmit(MyEventListEffect.NavigateToEventDetail(it))
        }
    }

    fun onSignInClick() {
        _effect.tryEmit(MyEventListEffect.NavigateToSignIn)
    }

}