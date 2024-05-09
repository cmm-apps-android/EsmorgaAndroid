package cmm.apps.esmorga

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import cmm.apps.domain.GetEventListUseCase
import cmm.apps.domain.GetEventListUseCaseImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EventListUiState(
    val loading: Boolean = false,
    val eventList: List<String> = emptyList()
)

class EventListViewModel(app: Application) : AndroidViewModel(app), DefaultLifecycleObserver {

    val useCase: GetEventListUseCase = GetEventListUseCaseImpl()

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

    private fun loadEvents(){
        viewModelScope.launch {
            val list = useCase.invoke()

            _uiState.value = EventListUiState(eventList = list.map { ev -> ev.name }) //TODO add mappers
            Log.d("THREAD", "loadEvents finished")
        }
    }

}