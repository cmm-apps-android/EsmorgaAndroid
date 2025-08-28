package cmm.apps.esmorga.view.createeventtype

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiModel
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScreenEffect
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScreenUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateEventTypeViewModel(
    private val eventForm: CreateEventFormUiModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateEventTypeScreenUiState(type = EventType.PARTY)
    )
    val uiState: StateFlow<CreateEventTypeScreenUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventTypeScreenEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventTypeScreenEffect> = _effect.asSharedFlow()

    fun onEventTypeSelected(type: EventType) {
        _uiState.value = _uiState.value.copy(
            type = type
        )
    }

    fun onBackClick() {
        _effect.tryEmit(CreateEventTypeScreenEffect.NavigateBack)
    }

    fun onNextClick() {
        val updatedForm = eventForm.copy(type = _uiState.value.type)
        _effect.tryEmit(CreateEventTypeScreenEffect.NavigateNext(updatedForm))
        println("TEST" + updatedForm)

    }
}