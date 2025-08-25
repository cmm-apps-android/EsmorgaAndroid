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
) {
    fun hasAnyError() = currentPasswordError != null || newPasswordError != null || repeatPasswordError != null
    fun enableButton(currentPassword: String, newPassword: String, repeatPassword: String) =
        !hasAnyError() && currentPassword.isNotBlank() && newPassword.isNotBlank() && repeatPassword.isNotBlank()
}

sealed class ChangePasswordEffect {
    data class NavigateToLogin(val snackbarMessage: String = getChangePasswordSnackBarSuccess()) : ChangePasswordEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : ChangePasswordEffect()
    data class ShowNoConnectionSnackbar(val message: String = getNoInternetSnackbarMessage()) : ChangePasswordEffect()
}

object ChangePasswordViewHelper : KoinComponent {
    private val context: Context by inject()
    fun getChangePasswordSnackBarSuccess() = context.getString(R.string.password_set_snackbar)
    fun getNoInternetSnackbarMessage() = context.getString(R.string.snackbar_no_internet)

    fun getPassFieldErrorText(
        value: String,
        isValidCondition: Boolean,
        reusedError: Boolean = false,
        mismatchError: Boolean = false,
    ) : String? {
        val isBlank = value.isBlank()
        val isValid = value.isEmpty() || isValidCondition
        return when {
            isBlank -> context.getString(R.string.inline_error_empty_field)
            !isValid -> context.getString(R.string.inline_error_password)
            reusedError -> context.getString(R.string.registration_reused_password_error)
            mismatchError -> context.getString(R.string.registration_password_mismatch_error)
            else -> null
        }

    }
}