package cmm.apps.esmorga

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class EventListUiState(
    val eventList: List<String> = emptyList()
)

class EventListViewModel(val app: Application) : AndroidViewModel(app), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        _uiState.value = EventListUiState(listOf("Test1","Test2","Test3","Test4","Test5"))
    }

}