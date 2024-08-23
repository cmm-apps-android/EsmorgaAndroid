package cmm.apps.esmorga.view.registration.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.registration.model.RegistrationViewHelper.getEsmorgaErrorScreenArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


data class RegistrationUiState(
    val loading: Boolean = false,
    val nameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passError: String? = null,
    val repeatPassError: String? = null
) {
    fun hasAnyError() = nameError != null || lastNameError != null || emailError != null || passError != null || repeatPassError != null
}

sealed class RegistrationEffect {
    data object ShowNoNetworkSnackbar : RegistrationEffect()
    data object NavigateToEventList : RegistrationEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaErrorScreenArguments()) : RegistrationEffect()
}

object RegistrationViewHelper : KoinComponent {
    private val context: Context by inject()
    fun getEsmorgaErrorScreenArguments() = EsmorgaErrorScreenArguments(
        title = context.getString(R.string.default_error_title),
        buttonText = context.getString(R.string.default_error_button)
    )

    fun getNameErrorText() = context.getString(R.string.registration_name_last_name_invalid)
    fun getLastNameErrorText() = context.getString(R.string.registration_name_last_name_invalid)
    fun getEmailErrorText() = context.getString(R.string.registration_email_invalid)
    fun getEmailAlreadyInUseErrorText() = context.getString(R.string.registration_email_already_used)
    fun getPasswordErrorText() = context.getString(R.string.registration_password_invalid)
    fun getRepeatPasswordErrorText() = context.getString(R.string.registration_password_mismatch_error)
    fun getEmptyFieldErrorText() = context.getString(R.string.registration_empty_field)
}