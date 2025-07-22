package cmm.apps.esmorga.datasource_remote.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel
import cmm.apps.esmorga.domain.user.model.RoleType

fun UserRemoteModel.toUserDataModel(): UserDataModel {
    val parseRoleType = try {
        RoleType.valueOf(this.remoteProfile.remoteRole.uppercase())
    } catch (e: Exception) {
        RoleType.USER
    }
    return UserDataModel(
        dataName = remoteProfile.remoteName,
        dataLastName = remoteProfile.remoteLastName,
        dataEmail = remoteProfile.remoteEmail,
        dataAccessToken = remoteAccessToken,
        dataRefreshToken = remoteRefreshToken,
        dataRole = parseRoleType
    )
}