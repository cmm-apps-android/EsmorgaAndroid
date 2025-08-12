package cmm.apps.esmorga.view.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.device.ShowDeviceIdIfNeededUseCase
import cmm.apps.esmorga.view.welcome.model.WelcomeEffect
import cmm.apps.esmorga.view.welcome.model.WelcomeUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val showDeviceIdNeededUseCase: ShowDeviceIdIfNeededUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState().createDefaultWelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<WelcomeEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<WelcomeEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            val result = showDeviceIdNeededUseCase()
            result
                .onSuccess { deviceId ->
                    _uiState.value = _uiState.value.copy(deviceId = deviceId)
                }

        }
    }

    fun onPrimaryButtonClicked() {
        _effect.tryEmit(WelcomeEffect.NavigateToLogin)
    }

    fun onSecondaryButtonClicked() {
        _effect.tryEmit(WelcomeEffect.NavigateToEventList)
    }
}