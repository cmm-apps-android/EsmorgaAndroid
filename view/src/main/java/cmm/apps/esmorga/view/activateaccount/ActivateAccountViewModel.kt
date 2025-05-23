package cmm.apps.esmorga.view.activateaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.account.ActivateAccountUseCase
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountEffect
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivateAccountViewModel(
    private val verificationCode: String,
    private val activateAccountUseCase: ActivateAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivateAccountUiState())
    val uiState: StateFlow<ActivateAccountUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ActivateAccountEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<ActivateAccountEffect> = _effect.asSharedFlow()

    private var failedAttempts = 0

    init {
        activateAccount(verificationCode)
    }

    fun activateAccount(verificationCode: String) {

        viewModelScope.launch {
            val result = activateAccountUseCase(verificationCode)

            result.onSuccess {
                _uiState.value = ActivateAccountUiState(isLoading = false)
            }.onFailure {
                _effect.tryEmit(ActivateAccountEffect.ShowFullScreenError())
            }
        }
    }
}
