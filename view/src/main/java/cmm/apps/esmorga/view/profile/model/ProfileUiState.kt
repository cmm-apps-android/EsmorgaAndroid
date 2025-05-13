package cmm.apps.esmorga.view.profile.model

import android.content.Context
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.profile.model.ProfileViewHelper.getEsmorgaNoNetworkScreenArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class ProfileUiState(
    val user: User? = null,
)

object ProfileViewHelper : KoinComponent {
    val context: Context by inject()
    fun getEsmorgaNoNetworkScreenArguments() = EsmorgaErrorScreenArguments(
        animation = R.raw.no_connection_anim,
        title = context.getString(R.string.screen_no_connection_title),
        subtitle = context.getString(R.string.screen_no_connection_body),
        buttonText = context.getString(R.string.button_ok)
    )
}

sealed class ProfileEffect {

    data class ShowNoNetworkError(val esmorgaNoNetworkArguments: EsmorgaErrorScreenArguments = getEsmorgaNoNetworkScreenArguments()) : ProfileEffect()
    data object NavigateToChangePassword : ProfileEffect()
    data object NavigateToLogOut : ProfileEffect()
    data object NavigateToLogIn : ProfileEffect()
}