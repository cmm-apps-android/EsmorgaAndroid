package cmm.apps.esmorga.view.login.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class LoginUiState(
    val loading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    fun hasAnyError() = emailError != null || passwordError != null
}

sealed class LoginEffect {
    data object NavigateToRegistration : LoginEffect()
    data object ShowNoNetworkSnackbar : LoginEffect()
    data object NavigateToEventList : LoginEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : LoginEffect()
    data object NavigateToForgotPassword : LoginEffect()
    data class ShowInitSnackbar(val message: String) : LoginEffect()
}

object LoginViewHelper : KoinComponent {
    private val context: Context by inject()

    fun getEmailErrorText() = context.getString(R.string.inline_error_email)
    fun getPasswordErrorText() = context.getString(R.string.inline_error_password)
    fun getEmptyFieldErrorText() = context.getString(R.string.inline_error_empty_field)

    fun getFieldErrorText(
        value: String,
        errorTextProvider: String,
        errorTextEmpty: String,
        acceptsEmpty: Boolean,
        nonEmptyCondition: Boolean,
        mismatchErrorCondition: Boolean = false,
        mismatchErrorText: String? = null,
        reusedErrorCondition: Boolean = false,
        reusedErrorText: String? = null
    ): String? {
        val isBlank = value.isBlank()
        val isValid = value.isEmpty() || nonEmptyCondition

        return when {
            !acceptsEmpty && isBlank -> errorTextEmpty
            !isValid -> errorTextProvider
            !mismatchErrorCondition -> mismatchErrorText
            !reusedErrorCondition -> reusedErrorText
            else -> null
        }
    }
}