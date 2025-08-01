package cmm.apps.esmorga.view.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.PerformChangePasswordUseCase
import cmm.apps.esmorga.domain.user.model.User.Companion.PASSWORD_REGEX
import cmm.apps.esmorga.view.changepassword.model.ChangePasswordEffect
import cmm.apps.esmorga.view.changepassword.model.ChangePasswordUiState
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getEmptyFieldErrorText
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getFieldErrorText
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getPasswordErrorText
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val changePasswordUseCase: PerformChangePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<ChangePasswordEffect> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<ChangePasswordEffect> = _effect


    fun onChangePasswordClicked(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = ChangePasswordUiState(isLoading = true)
            changePasswordUseCase(currentPassword, newPassword)
                .onSuccess {
                    _uiState.value = ChangePasswordUiState(isLoading = false)
                    _effect.tryEmit(ChangePasswordEffect.NavigateToLogin())
                }
                .onFailure {
                    _uiState.value = ChangePasswordUiState(isLoading = false)
                    _effect.tryEmit(ChangePasswordEffect.ShowFullScreenError())
                }.onNoConnectionError {
                    _uiState.value = ChangePasswordUiState(isLoading = false)
                    _effect.tryEmit(ChangePasswordEffect.ShowNoConnectionSnackbar())
                }
        }

    }

    fun validateField(type: ChangePasswordField, password: String, newPass: String?, repeatNewPass: String?, hasFocused: Boolean = false) {
        if (hasFocused) {
            when (type) {
                ChangePasswordField.PASS -> _uiState.value =
                    _uiState.value.copy(
                        currentPasswordError = getFieldErrorText(
                            password,
                            getPasswordErrorText(),
                            getEmptyFieldErrorText(),
                            false,
                            password.matches(PASSWORD_REGEX.toRegex())
                        )
                    )

                ChangePasswordField.NEW_PASS -> _uiState.value =
                    _uiState.value.copy(
                        newPasswordError = getFieldErrorText(
                            newPass.orEmpty(),
                            getPasswordErrorText(),
                            getEmptyFieldErrorText(),
                            false,
                            newPass.orEmpty().matches(PASSWORD_REGEX.toRegex())
                        )
                    )

                ChangePasswordField.REPEAT_NEW_PASS -> _uiState.value =
                    _uiState.value.copy(
                        repeatPasswordError = getFieldErrorText(
                            repeatNewPass.orEmpty(),
                            getPasswordErrorText(),
                            getEmptyFieldErrorText(),
                            false,
                            newPass.orEmpty().matches(PASSWORD_REGEX.toRegex())
                        )
                    )
            }
        }

    }

    enum class ChangePasswordField {
        PASS,
        NEW_PASS,
        REPEAT_NEW_PASS

    }
}