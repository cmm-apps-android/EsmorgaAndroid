package cmm.apps.esmorga.view.createevent

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateEventFlowViewModel : ViewModel() {

    private val _eventForm = MutableStateFlow(CreateEventForm())
    val eventForm: StateFlow<CreateEventForm> = _eventForm.asStateFlow()

    fun updateTitle(name: String, description: String?) {
        _eventForm.update { current ->
            current.copy(
                name = name,
                description = description
            )
        }
    }

    fun updateType(type: EventType) {
        _eventForm.update { current ->
            current.copy(type = type)
        }
    }

    fun updateDate(date: String, joinDeadline: String?) {
        _eventForm.update { current ->
            current.copy(
                date = date,
                joinDeadline = joinDeadline
            )
        }
    }

    fun updateLocation(location: EventLocation, maxCapacity: Int?) {
        _eventForm.update { current ->
            current.copy(
                location = location,
                maxCapacity = maxCapacity
            )
        }
    }

    fun updateImage(imageUrl: String?) {
        _eventForm.update { current ->
            current.copy(imageUrl = imageUrl)
        }
    }

    fun reset() {
        _eventForm.value = CreateEventForm()
    }
}
