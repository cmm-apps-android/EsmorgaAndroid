package cmm.apps.esmorga.view.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.PerformRecoverPasswordUseCase
import cmm.apps.esmorga.domain.user.PerformRegistrationConfirmationUseCase
import cmm.apps.esmorga.domain.user.model.User.Companion.EMAIL_REGEX
import cmm.apps.esmorga.view.login.model.LoginUiState
import cmm.apps.esmorga.view.login.model.LoginViewHelper
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getEmptyFieldErrorText
import cmm.apps.esmorga.view.password.model.RecoverPasswordEffect
import cmm.apps.esmorga.view.password.model.RecoverPasswordUiState
import cmm.apps.esmorga.view.password.model.RecoverPasswordViewHelper.getEmailErrorText
import cmm.apps.esmorga.view.registration.model.RegistrationConfirmationEffect
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RecoverPasswordViewModel(val performRecoverPasswordUseCase: PerformRecoverPasswordUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(RecoverPasswordUiState())
    val uiState: StateFlow<RecoverPasswordUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<RecoverPasswordEffect> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<RecoverPasswordEffect> = _effect.asSharedFlow()

    fun onResendEmailClicked(email: String) {
        validateEmail(email, false)
        if (!_uiState.value.hasAnyError()) {
            viewModelScope.launch {
                val result = performRecoverPasswordUseCase(email.trim())
                result.onSuccess {
                    _effect.tryEmit(RecoverPasswordEffect.ShowSnackbarSuccess())
                }.onFailure {
                    _effect.tryEmit(RecoverPasswordEffect.ShowFullScreenError())
                }
            }
        }
    }

    fun validateEmail(email: String, acceptsEmpty: Boolean = true) {
        _uiState.value = _uiState.value.copy(emailError = getFieldErrorText(email, getEmailErrorText(), acceptsEmpty, email.matches(EMAIL_REGEX.toRegex())))
    }

    private fun getFieldErrorText(
        value: String,
        errorTextProvider: String,
        acceptsEmpty: Boolean,
        nonEmptyCondition: Boolean
    ): String? {
        val isBlank = value.isBlank()
        val isValid = value.isEmpty() || nonEmptyCondition

        return when {
            !acceptsEmpty && isBlank -> getEmptyFieldErrorText()
            !isValid -> errorTextProvider
            else -> null
        }
    }
}