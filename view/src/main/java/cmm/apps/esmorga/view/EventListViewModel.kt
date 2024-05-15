package cmm.apps.esmorga.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.GetEventListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.TimeZone

data class EventListUiState(
    val loading: Boolean = false,
    val eventList: List<String> = emptyList()
)

class EventListViewModel(app: Application, private val useCase: GetEventListUseCase) : AndroidViewModel(app), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d("THREAD", "onStart started")

        _uiState.value = EventListUiState(loading = true)

        Log.d("THREAD", "onStart finished")
    }

    private fun loadEvents() {
        viewModelScope.launch {
            val list = useCase.invoke()

            _uiState.value = EventListUiState(eventList = list.map {
                ev -> "${ev.name} - ${ev.date.format(DateTimeFormatter.ofPattern("dd' de 'MMMM' a las 'HH:mm").withZone(TimeZone.getDefault().toZoneId()))}"
            }) //TODO add mappers
            Log.d("THREAD", "loadEvents finished")
        }
    }

}