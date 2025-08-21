package cmm.apps.esmorga.view.changepassword.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.changepassword.model.ChangePasswordViewHelper.getChangePasswordSnackBarSuccess
import cmm.apps.esmorga.view.changepassword.model.ChangePasswordViewHelper.getNoInternetSnackbarMessage
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val repeatPasswordError: String? = null
)

sealed class ChangePasswordEffect {
    data class NavigateToLogin(val snackbarMessage: String = getChangePasswordSnackBarSuccess()) : ChangePasswordEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : ChangePasswordEffect()
    data class ShowNoConnectionSnackbar(val message: String = getNoInternetSnackbarMessage()) : ChangePasswordEffect()
}

object ChangePasswordViewHelper : KoinComponent {
    private val context: Context by inject()
    fun getChangePasswordSnackBarSuccess() = context.getString(R.string.password_set_snackbar)
    fun getNoInternetSnackbarMessage() = context.getString(R.string.snackbar_no_internet)
    fun getRegistrationPasswordMismatchError() = context.getString(R.string.registration_password_mismatch_error)
    fun getRegistrationReusedPasswordError() = context.getString(R.string.registration_reused_password_error)
}