package cmm.apps.esmorga.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.view.login.model.LoginEffect
import cmm.apps.esmorga.view.login.model.LoginUiState
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getEmailErrorText
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getPasswordErrorText
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(app: Application, private val performLoginUseCase: PerformLoginUseCase) : AndroidViewModel(app) {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<LoginEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    companion object {
        private const val EMAIL_REGEX = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        private const val PASSWORD_REGEX = "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+']).+"
    }

    fun onLoginClicked(): (String, String) -> Unit {
        return { email, password ->
            validateEmail(email)
            validatePass(password)
            if (_uiState.value.emailError == null && _uiState.value.passwordError == null) {
                viewModelScope.launch {
                    _uiState.value = LoginUiState(loading = true)
                    val result = performLoginUseCase(email, password)
                    result.onSuccess {
                        _effect.tryEmit(LoginEffect.NavigateToEventList)
                    }.onFailure { error ->
                        if (error is EsmorgaException) {
                            _uiState.value = _uiState.value.copy(loading = false)
                            if (error.code == ErrorCodes.NO_CONNECTION) {
                                _effect.tryEmit(LoginEffect.ShowNoNetworkSnackbar)
                            } else {
                                _effect.tryEmit(LoginEffect.ShowFullScreenError())
                            }
                        }
                    }
                }
            }
        }
    }

    fun validateEmail(email: String) {
        if (!email.matches(EMAIL_REGEX.toRegex()) && email.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(emailError = getEmailErrorText())
        } else {
            _uiState.value = _uiState.value.copy(emailError = null)
        }
    }

    fun validatePass(pass: String) {
        if (!pass.matches(PASSWORD_REGEX.toRegex()) && pass.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(passwordError = getPasswordErrorText())
        } else {
            _uiState.value = _uiState.value.copy(passwordError = null)
        }
    }
}