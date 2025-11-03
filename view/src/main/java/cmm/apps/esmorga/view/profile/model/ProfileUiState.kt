package cmm.apps.esmorga.view.profile.model

import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaNoNetworkScreenArguments

data class ProfileUiState(
    val user: User? = null,
)

sealed class ProfileEffect {

    data class ShowNoNetworkError(val esmorgaNoNetworkArguments: EsmorgaErrorScreenArguments = getEsmorgaNoNetworkScreenArguments()) : ProfileEffect()
    data object NavigateToChangePassword : ProfileEffect()
    data object NavigateToLogOut : ProfileEffect()
    data object NavigateToLogIn : ProfileEffect()
}