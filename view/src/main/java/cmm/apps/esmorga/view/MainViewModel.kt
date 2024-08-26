package cmm.apps.esmorga.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(private val getSavedUserUseCase: GetSavedUserUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = getSavedUserUseCase.invoke()
            result.onSuccess {
                _uiState.value = MainUiState(loading = false, loggedIn = true)
            }.onFailure { _ ->
                _uiState.value = MainUiState(loading = false, loggedIn = false)
            }
        }
    }
}

data class MainUiState(
    val loading: Boolean = true,
    val loggedIn: Boolean = false
)