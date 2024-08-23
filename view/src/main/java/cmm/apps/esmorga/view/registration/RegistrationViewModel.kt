package cmm.apps.esmorga.view.registration

import androidx.lifecycle.ViewModel
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


class RegistrationViewModel(private val performRegistrationUserCase: PerformRegistrationUserCase) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<RegistrationEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<RegistrationEffect> = _effect.asSharedFlow()

    fun onRegisterClicked(name: String, lastName: String, email: String, password: String, repeatedPassword: String) {
        validateField(field = RegistrationField.NAME, value = name, acceptsEmpty = false)
        validateField(field = RegistrationField.LAST_NAME, value = lastName, acceptsEmpty = false)
        validateField(field = RegistrationField.EMAIL, value = email, acceptsEmpty = false)
        validateField(field = RegistrationField.PASS, value = password, acceptsEmpty = false)
        validateField(field = RegistrationField.REPEAT_PASS, value = repeatedPassword, comparisonField = password, acceptsEmpty = false)
        if (!_uiState.value.hasAnyError()) {
            viewModelScope.launch {
                _uiState.value = RegistrationUiState(loading = true)
                val result = performRegistrationUserCase(name.trim(), lastName.trim(), email.trim(), password.trim())
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

    fun validateField(field: RegistrationField, value: String, comparisonField: String? = null, acceptsEmpty: Boolean = true) {
        when (field) {
            RegistrationField.NAME -> _uiState.value =
                _uiState.value.copy(nameError = getFieldErrorText(value, getNameErrorText(), acceptsEmpty, value.matches(NAME_REGEX.toRegex())))

            RegistrationField.LAST_NAME -> _uiState.value =
                _uiState.value.copy(lastNameError = getFieldErrorText(value, getLastNameErrorText(), acceptsEmpty, value.matches(NAME_REGEX.toRegex())))

            RegistrationField.EMAIL -> _uiState.value =
                _uiState.value.copy(emailError = getFieldErrorText(value, getEmailErrorText(), acceptsEmpty, value.matches(EMAIL_REGEX.toRegex())))

            RegistrationField.PASS -> _uiState.value =
                _uiState.value.copy(passError = getFieldErrorText(value, getPasswordErrorText(), acceptsEmpty, value.matches(PASSWORD_REGEX.toRegex())))

            RegistrationField.REPEAT_PASS -> _uiState.value =
                _uiState.value.copy(repeatPassError = getFieldErrorText(value, getRepeatPasswordErrorText(), acceptsEmpty, value == comparisonField))
        }
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

    fun onFieldChanged(field: RegistrationField) {
        when (field) {
            RegistrationField.NAME -> _uiState.value = _uiState.value.copy(nameError = null)
            RegistrationField.LAST_NAME -> _uiState.value = _uiState.value.copy(lastNameError = null)
            RegistrationField.EMAIL -> _uiState.value = _uiState.value.copy(emailError = null)
            RegistrationField.PASS -> _uiState.value = _uiState.value.copy(passError = null)
            RegistrationField.REPEAT_PASS -> _uiState.value = _uiState.value.copy(repeatPassError = null)
        }
    }

}

enum class RegistrationField {
    NAME,
    LAST_NAME,
    EMAIL,
    PASS,
    REPEAT_PASS
}