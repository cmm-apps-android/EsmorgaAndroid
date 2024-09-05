package cmm.apps.esmorga.view.login.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getEsmorgaErrorScreenArguments
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
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaErrorScreenArguments()) : LoginEffect()
}

object LoginViewHelper : KoinComponent{
    private val context : Context by inject()
    fun getEsmorgaErrorScreenArguments() = EsmorgaErrorScreenArguments(
        title = context.getString(R.string.default_error_title_expanded),
        buttonText = context.getString(R.string.button_retry)
    )

    fun getEmailErrorText() = context.getString(R.string.inline_error_email)
    fun getPasswordErrorText() = context.getString(R.string.inline_error_password)
    fun getEmptyFieldErrorText() = context.getString(R.string.inline_error_empty_field)
}