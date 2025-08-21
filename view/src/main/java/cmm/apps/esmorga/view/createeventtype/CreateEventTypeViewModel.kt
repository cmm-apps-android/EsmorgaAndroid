package cmm.apps.esmorga.view.createeventtype

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.R
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
    private val eventName: String,
    private val description: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CreateEventTypeScreenUiState(
            selectedEventType = EventType.PARTY
        )
    )
    val uiState: StateFlow<CreateEventTypeScreenUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventTypeScreenEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventTypeScreenEffect> = _effect.asSharedFlow()

    fun onEventTypeSelected(type: EventType) {
        _uiState.value = _uiState.value.copy(selectedEventType = type)
    }

    fun onBackClick() {
        _effect.tryEmit(CreateEventTypeScreenEffect.NavigateBack)
    }

    fun onNextClick() {
        val state = _uiState.value
        _effect.tryEmit(
            CreateEventTypeScreenEffect.NavigateNext(
                eventName = eventName,
                description = description,
                eventType = state.selectedEventType.name
            )
        )
    }
}

fun EventType.getUiTextRes(): Int = when(this) {
    EventType.PARTY -> R.string.step_2_option_party
    EventType.SPORT -> R.string.step_2_option_sport
    EventType.FOOD -> R.string.step_2_option_food
    EventType.CHARITY -> R.string.step_2_option_charity
    EventType.GAMES -> R.string.step_2_option_games
}
