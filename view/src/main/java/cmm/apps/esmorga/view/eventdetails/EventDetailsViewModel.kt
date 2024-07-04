package cmm.apps.esmorga.view.eventdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventlist.mapper.EventUiMapper.toEventUiDetails
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    app: Application,
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    private val eventId: String
) : AndroidViewModel(app) {
    private val _uiState = MutableStateFlow(EventDetailsUiState())
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventDetailsEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventDetailsEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            val result = getEventDetailsUseCase(eventId)
            result.onSuccess {
                _uiState.value = it.data.toEventUiDetails()
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

}