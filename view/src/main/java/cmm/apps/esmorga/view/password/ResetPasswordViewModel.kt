package cmm.apps.esmorga.view.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.model.User.Companion.PASSWORD_REGEX
import cmm.apps.esmorga.domain.user.repository.PerformResetPasswordUseCase
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getFieldErrorText
import cmm.apps.esmorga.view.password.model.ResetPasswordEffect
import cmm.apps.esmorga.view.password.model.ResetPasswordUiState
import cmm.apps.esmorga.view.password.model.ResetPasswordViewHelper.getEmptyFieldErrorText
import cmm.apps.esmorga.view.password.model.ResetPasswordViewHelper.getPasswordErrorText
import cmm.apps.esmorga.view.password.model.ResetPasswordViewHelper.getRepeatPasswordErrorText
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ResetPasswordViewModel(val performResetPasswordUseCase: PerformResetPasswordUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<ResetPasswordEffect> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<ResetPasswordEffect> = _effect.asSharedFlow()

    fun onResetPasswordClicked(code: String, password: String) {
        if (!_uiState.value.hasAnyError()) {
            viewModelScope.launch {
                _uiState.value = ResetPasswordUiState(isLoading = true)
                val result = performResetPasswordUseCase(code, password)
                result.onSuccess {
                    _effect.tryEmit(ResetPasswordEffect.NavigateToLogin())
                }.onFailure {
                    _uiState.value = ResetPasswordUiState(isLoading = false)
                    _effect.tryEmit(ResetPasswordEffect.ShowFullScreenError())
                }.onNoConnectionError {
                    _uiState.value = ResetPasswordUiState(isLoading = false)
                    _effect.tryEmit(ResetPasswordEffect.ShowNoConnectionSnackbar())
                }
            }
        }
    }

    fun onValueChange(password: String, repeatedPassword: String) {
        validateField(type = ResetPasswordField.PASS, value = password, acceptsEmpty = false)
        validateField(type = ResetPasswordField.REPEAT_PASS, value = repeatedPassword, comparisonField = password, acceptsEmpty = false)
    }

    fun validateField(type: ResetPasswordField, value: String, comparisonField: String? = null, acceptsEmpty: Boolean = true) {
        when (type) {
            ResetPasswordField.PASS -> _uiState.value =
                _uiState.value.copy(
                    passwordError = getFieldErrorText(
                        value,
                        getPasswordErrorText(),
                        getEmptyFieldErrorText(),
                        acceptsEmpty,
                        value.matches(PASSWORD_REGEX.toRegex())
                    )
                )

            ResetPasswordField.REPEAT_PASS -> _uiState.value =
                _uiState.value.copy(
                    repeatPasswordError = getFieldErrorText(
                        value,
                        getRepeatPasswordErrorText(),
                        getEmptyFieldErrorText(),
                        acceptsEmpty,
                        value == comparisonField
                    )
                )

        }
    }

    enum class ResetPasswordField {
        PASS,
        REPEAT_PASS
    }
}