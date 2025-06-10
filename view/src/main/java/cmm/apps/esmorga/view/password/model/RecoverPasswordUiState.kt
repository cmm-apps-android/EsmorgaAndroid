package cmm.apps.esmorga.view.password.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments
import cmm.apps.esmorga.view.password.model.RecoverPasswordViewHelper.getEsmorgaMessageSnackBarSuccess
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class RecoverPasswordUiState(
    val emailError: String? = null
) {
    fun hasAnyError() = emailError != null
}

sealed class RecoverPasswordEffect {
    data class ShowSnackbarSuccess(val message: String = getEsmorgaMessageSnackBarSuccess()) : RecoverPasswordEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : RecoverPasswordEffect()
}

object RecoverPasswordViewHelper : KoinComponent {
    private val context: Context by inject()
    fun getEsmorgaMessageSnackBarSuccess() = context.getString(R.string.forgot_password_snackbar_success)
    fun getEmailErrorText() = context.getString(R.string.invalid_credentials_error)
}