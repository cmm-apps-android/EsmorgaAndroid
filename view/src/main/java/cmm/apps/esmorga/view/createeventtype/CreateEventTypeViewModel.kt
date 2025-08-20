package cmm.apps.esmorga.view.createeventtype

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScrrenEffect
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScrrenUi
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
        CreateEventTypeScrrenUi(
            eventName = eventName,
            description = description,
            selectedEventType = EventType.Party
        )
    )
    val uiState: StateFlow<CreateEventTypeScrrenUi> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventTypeScrrenEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventTypeScrrenEffect> = _effect.asSharedFlow()

    fun onEventTypeSelected(type: EventType) {
        _uiState.value = _uiState.value.copy(selectedEventType = type)
    }

    fun onBackClick() {
        _effect.tryEmit(CreateEventTypeScrrenEffect.NavigateBack)
    }

    fun onNextClick() {
        val state = _uiState.value
        _effect.tryEmit(
            CreateEventTypeScrrenEffect.NavigateNext(
                eventName = state.eventName,
                description = state.description,
                eventType = state.selectedEventType?.backendValue
            )
        )
    }
}

enum class EventType(val uiTextRes: Int, val backendValue: String) {
    Party(cmm.apps.esmorga.view.R.string.step_2_option_party, "Party"),
    Sport(cmm.apps.esmorga.view.R.string.step_2_option_sport, "Sport"),
    Food(cmm.apps.esmorga.view.R.string.step_2_option_food, "Food"),
    Charity(cmm.apps.esmorga.view.R.string.step_2_option_charity, "Charity"),
    Games(cmm.apps.esmorga.view.R.string.step_2_option_games, "Games")
}
