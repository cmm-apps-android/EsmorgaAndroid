package cmm.apps.esmorga.view.password.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.password.model.ResetPasswordViewHelper.getEsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.password.model.ResetPasswordViewHelper.getEsmorgaMessageSnackBarSuccess
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val passwordError: String? = null,
    val repeatPasswordError: String? = null
) {
    fun hasAnyError() = passwordError != null || repeatPasswordError != null
    fun enableButton(password: String, repeatPassword: String) = !hasAnyError() && (password.isNotBlank() || repeatPassword.isNotBlank())
}

sealed class ResetPasswordEffect {
    data class NavigateToLogin(val snackbarMessage: String = getEsmorgaMessageSnackBarSuccess()) : ResetPasswordEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaErrorScreenArguments()) : ResetPasswordEffect()
}

object ResetPasswordViewHelper : KoinComponent {
    private val context: Context by inject()
    fun getEsmorgaMessageSnackBarSuccess() = context.getString(R.string.password_set_snackbar)
    fun getPasswordErrorText() = context.getString(R.string.inline_error_password_invalid)
    fun getRepeatPasswordErrorText() = context.getString(R.string.inline_error_password_mismatch)
    fun getEmptyFieldErrorText() = context.getString(R.string.inline_error_empty_field)
    fun getEsmorgaErrorScreenArguments() = EsmorgaErrorScreenArguments(
        title = context.getString(R.string.default_error_title_expanded),
        buttonText = context.getString(R.string.button_retry)
    )
}