package cmm.apps.esmorga.view.createevent.createeventtype

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createevent.CreateEventFlowViewModel
import cmm.apps.esmorga.view.createevent.createeventtype.model.CreateEventTypeScreenEffect
import cmm.apps.esmorga.view.createevent.createeventtype.model.CreateEventTypeScreenUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateEventFormTypeViewModel(
    private val createEventFlowViewModel: CreateEventFlowViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventTypeScreenUiState(type = createEventFlowViewModel.eventForm.value.type ?: EventType.PARTY))
    val uiState: StateFlow<CreateEventTypeScreenUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventTypeScreenEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventTypeScreenEffect> = _effect.asSharedFlow()

    init {
        restoreFromFlow()
    }

    private fun restoreFromFlow() {
        val selectedType = createEventFlowViewModel.eventForm.value.type ?: EventType.PARTY
        if (_uiState.value.type == selectedType) return

        _uiState.update { it.copy(type = selectedType) }
    }

    fun onEventTypeSelected(type: EventType) {
        _uiState.update { it.copy(type = type) }
    }

    fun onBackClick() {
        createEventFlowViewModel.updateType(_uiState.value.type)
        _effect.tryEmit(CreateEventTypeScreenEffect.NavigateBack)
    }

    fun onNextClick() {
        createEventFlowViewModel.updateType(_uiState.value.type)
        _effect.tryEmit(CreateEventTypeScreenEffect.NavigateNext)
    }
}