package cmm.apps.esmorga.view.registration.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.registration.model.RegistrationConfirmationViewHelper.getEsmorgaMessageSnackBarFailure
import cmm.apps.esmorga.view.registration.model.RegistrationConfirmationViewHelper.getEsmorgaMessageSnackBarSuccess
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class RegistrationConfirmationEffect {
    data object NavigateToEmailApp : RegistrationConfirmationEffect()
    data class ShowSnackbarSuccess(val message: String = getEsmorgaMessageSnackBarSuccess()) :
        RegistrationConfirmationEffect()

    data class ShowSnackbarFailure(val message: String = getEsmorgaMessageSnackBarFailure()) :
        RegistrationConfirmationEffect()
}

object RegistrationConfirmationViewHelper : KoinComponent {
    private val context: Context by inject()
    fun getEsmorgaMessageSnackBarSuccess() =
        context.getString(R.string.register_resend_code_success)

    fun getEsmorgaMessageSnackBarFailure() =
        context.getString(R.string.register_resend_code_error)
}