package cmm.apps.esmorga.view.activateaccount.model

import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getEsmorgaErrorScreenArguments

data class ActivateAccountUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val failedAttemps: Int = 0
)

enum class ActivateAccountError{
    INVALUD_OR_EXPIRED,
    UNKNOWN
}

sealed class ActivateAccountEffect{
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaErrorScreenArguments()): ActivateAccountEffect()
}