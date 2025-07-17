package cmm.apps.esmorga.data.user.model

import cmm.apps.esmorga.domain.user.model.RoleType

data class UserDataModel(
    val dataName: String,
    val dataLastName: String,
    val dataEmail: String,
    val dataAccessToken: String?,
    val dataRefreshToken: String?,
    val dataRole: RoleType
)