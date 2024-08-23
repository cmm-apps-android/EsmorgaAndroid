package cmm.apps.esmorga.view.welcome

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.view.welcome.model.WelcomeEffect
import cmm.apps.esmorga.view.welcome.model.WelcomeUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class WelcomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState().createDefaultWelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<WelcomeEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<WelcomeEffect> = _effect.asSharedFlow()

    fun onPrimaryButtonClicked() {
        _effect.tryEmit(WelcomeEffect.NavigateToLogin)
    }

    fun onSecondaryButtonClicked() {
        _effect.tryEmit(WelcomeEffect.NavigateToEventList)
    }

}