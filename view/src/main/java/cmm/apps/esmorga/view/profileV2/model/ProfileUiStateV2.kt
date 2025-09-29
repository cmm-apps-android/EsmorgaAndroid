package cmm.apps.esmorga.view.profileV2.model

import cmm.apps.esmorga.domain.user.model.User

data class ProfileUiStateV2(
    val user: User? = null
    )

sealed class ProfileEffectV2{
    data object NavigateToLogIn: ProfileEffectV2()
    data object NavigateToChangePassword: ProfileEffectV2()
}