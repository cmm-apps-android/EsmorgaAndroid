package cmm.apps.esmorga.view.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.domain.user.model.User.Companion.EMAIL_REGEX
import cmm.apps.esmorga.domain.user.model.User.Companion.NAME_REGEX
import cmm.apps.esmorga.domain.user.model.User.Companion.PASSWORD_REGEX
import cmm.apps.esmorga.view.registration.model.RegistrationEffect
import cmm.apps.esmorga.view.registration.model.RegistrationUiState
import cmm.apps.esmorga.view.registration.model.RegistrationViewHelper.getEmailAlreadyInUseErrorText
import cmm.apps.esmorga.view.registration.model.RegistrationViewHelper.getEmailErrorText
import cmm.apps.esmorga.view.registration.model.RegistrationViewHelper.getEmptyFieldErrorText
import cmm.apps.esmorga.view.registration.model.RegistrationViewHelper.getLastNameErrorText
import cmm.apps.esmorga.view.registration.model.RegistrationViewHelper.getNameErrorText
import cmm.apps.esmorga.view.registration.model.RegistrationViewHelper.getPasswordErrorText
import cmm.apps.esmorga.view.registration.model.RegistrationViewHelper.getRepeatPasswordErrorText
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RegistrationViewModel(app: Application, private val performRegistrationUserCase: PerformRegistrationUserCase) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<RegistrationEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<RegistrationEffect> = _effect.asSharedFlow()

    fun onRegisterClicked(name: String, lastName: String, email: String, password: String, repeatedPassword: String) {
        validateName(name, false)
        validateLastName(lastName, false)
        validateEmail(email, false)
        validatePass(password, false)
        validateRepeatedPass(password, repeatedPassword, false)
        if (!_uiState.value.hasAnyError()) {
            viewModelScope.launch {
                _uiState.value = RegistrationUiState(loading = true)
                val result = performRegistrationUserCase(name, lastName, email, password)
                result.onSuccess {
                    _effect.tryEmit(RegistrationEffect.NavigateToEventList)
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(loading = false)
                    when {
                        error is EsmorgaException && error.code == ErrorCodes.NO_CONNECTION -> _effect.tryEmit(RegistrationEffect.ShowNoNetworkSnackbar)
                        error is EsmorgaException && error.code == 409 -> _uiState.value = RegistrationUiState(nameError = getEmailAlreadyInUseErrorText())
                        else -> _effect.tryEmit(RegistrationEffect.ShowFullScreenError())
                    }
                }
            }
        }
    }

    fun validateName(name: String, acceptsEmpty: Boolean = true) {
        if(!acceptsEmpty && name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = getEmptyFieldErrorText())
        } else if (name.isNotEmpty() && !name.matches(NAME_REGEX.toRegex())) {
            _uiState.value = _uiState.value.copy(nameError = getNameErrorText())
        } else {
            _uiState.value = _uiState.value.copy(nameError = null)
        }
    }

    fun validateLastName(lastName: String, acceptsEmpty: Boolean = true) {
        if(!acceptsEmpty && lastName.isBlank()) {
            _uiState.value = _uiState.value.copy(lastNameError = getEmptyFieldErrorText())
        } else if (lastName.isNotEmpty() && !lastName.matches(NAME_REGEX.toRegex())) {
            _uiState.value = _uiState.value.copy(lastNameError = getLastNameErrorText())
        } else {
            _uiState.value = _uiState.value.copy(lastNameError = null)
        }
    }

    fun validateEmail(email: String, acceptsEmpty: Boolean = true) {
        if(!acceptsEmpty && email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = getEmptyFieldErrorText())
        } else if (email.isNotEmpty() && !email.matches(EMAIL_REGEX.toRegex())) {
            _uiState.value = _uiState.value.copy(emailError = getEmailErrorText())
        } else {
            _uiState.value = _uiState.value.copy(emailError = null)
        }
    }

    fun validatePass(pass: String, acceptsEmpty: Boolean = true) {
        if(!acceptsEmpty && pass.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = getEmptyFieldErrorText())
        } else if (pass.isNotEmpty() && !pass.matches(PASSWORD_REGEX.toRegex())) {
            _uiState.value = _uiState.value.copy(passwordError = getPasswordErrorText())
        } else {
            _uiState.value = _uiState.value.copy(passwordError = null)
        }
    }

    fun validateRepeatedPass(pass: String, repeatedPass: String, acceptsEmpty: Boolean = true) {
        if(!acceptsEmpty && repeatedPass.isBlank()) {
            _uiState.value = _uiState.value.copy(repeatPasswordError = getEmptyFieldErrorText())
        } else if (repeatedPass.isNotEmpty() && pass != repeatedPass) {
            _uiState.value = _uiState.value.copy(repeatPasswordError = getRepeatPasswordErrorText())
        } else {
            _uiState.value = _uiState.value.copy(repeatPasswordError = null)
        }
    }

}