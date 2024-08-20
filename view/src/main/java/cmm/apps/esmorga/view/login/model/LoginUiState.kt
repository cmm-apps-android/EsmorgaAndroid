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
        title = context.getString(R.string.default_error_title),
        buttonText = context.getString(R.string.default_error_button)
    )

    fun getEmailErrorText() = context.getString(R.string.login_email_invalid)
    fun getPasswordErrorText() = context.getString(R.string.login_password_invalid)
}