package cmm.apps.esmorga.view.activateaccount.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountHelper.getActivateAccountErrorScreenArguments
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountHelper.getActivateAccountLastTryErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class ActivateAccountUiState(
    val isLoading: Boolean = true,
    val error: String? = null
)

object ActivateAccountHelper : KoinComponent {
    private val context: Context by inject()

    fun getActivateAccountErrorScreenArguments(): EsmorgaErrorScreenArguments {
        return EsmorgaErrorScreenArguments(
            title = context.getString(R.string.register_confirmation_error_title),
            buttonText = context.getString(R.string.register_confirmation_button_retry)
        )
    }

    fun getActivateAccountLastTryErrorScreenArguments(): EsmorgaErrorScreenArguments {
        return EsmorgaErrorScreenArguments(
            title = context.getString(R.string.default_error_title),
            buttonText = context.getString(R.string.register_confirmation_button_cancel)
        )
    }
}

sealed class ActivateAccountEffect {
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getActivateAccountErrorScreenArguments()) : ActivateAccountEffect()
    data class ShowLastTryFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getActivateAccountLastTryErrorScreenArguments(), val redirectToWelcome: Boolean = true) : ActivateAccountEffect()
}