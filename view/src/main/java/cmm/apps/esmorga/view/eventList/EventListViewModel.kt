package cmm.apps.esmorga.view.eventList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.error.EsmorgaException
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.TimeZone

data class EventListUiState(
    val loading: Boolean = false,
    val eventList: List<String> = emptyList(),
    val error: String? = null
)

class EventListViewModel(app: Application, private val useCase: GetEventListUseCase) : AndroidViewModel(app), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        _uiState.value = EventListUiState(loading = true)
    }

    private fun loadEvents() {
        viewModelScope.launch {
            val result = useCase.invoke()

            if (result.isSuccess) {
                val list = result.getOrDefault(listOf())
                _uiState.value = EventListUiState(eventList = list.map { ev ->
                    "${ev.name} - ${ev.date.format(DateTimeFormatter.ofPattern("dd' de 'MMMM' a las 'HH:mm").withZone(TimeZone.getDefault().toZoneId()))}"
                })
            } else {
                val error = result.exceptionOrNull()
                if (error is EsmorgaException) {
                    _uiState.value = EventListUiState(error = "${error.source} error: ${error.message}")
                } else {
                    _uiState.value = EventListUiState(error = "Unknown error: ${error?.message}")
                }
            }
        }
    }

}