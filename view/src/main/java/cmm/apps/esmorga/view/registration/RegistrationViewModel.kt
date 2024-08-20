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
        _uiState.value = _uiState.value.copy(nameError = getFieldErrorText(name, getNameErrorText(), acceptsEmpty, name.matches(NAME_REGEX.toRegex())))
    }

    fun validateLastName(lastName: String, acceptsEmpty: Boolean = true) {
        _uiState.value = _uiState.value.copy(lastNameError = getFieldErrorText(lastName, getLastNameErrorText(), acceptsEmpty, lastName.matches(NAME_REGEX.toRegex())))
    }

    fun validateEmail(email: String, acceptsEmpty: Boolean = true) {
        _uiState.value = _uiState.value.copy(emailError = getFieldErrorText(email, getEmailErrorText(), acceptsEmpty, email.matches(EMAIL_REGEX.toRegex())))
    }

    fun validatePass(pass: String, acceptsEmpty: Boolean = true) {
        _uiState.value = _uiState.value.copy(passwordError = getFieldErrorText(pass, getPasswordErrorText(), acceptsEmpty, pass.matches(PASSWORD_REGEX.toRegex())))
    }

    fun validateRepeatedPass(pass: String, repeatedPass: String, acceptsEmpty: Boolean = true) {
        _uiState.value = _uiState.value.copy(repeatPasswordError = getFieldErrorText(repeatedPass, getRepeatPasswordErrorText(), acceptsEmpty, pass == repeatedPass))
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