package cmm.apps.esmorga.datasource_remote.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel
import cmm.apps.esmorga.domain.user.model.RoleType

fun UserRemoteModel.toUserDataModel(): UserDataModel {
    val parseRoleType = RoleType.fromString(remoteProfile.remoteRole)
    return UserDataModel(
        dataName = remoteProfile.remoteName,
        dataLastName = remoteProfile.remoteLastName,
        dataEmail = remoteProfile.remoteEmail,
        dataRole = parseRoleType
    )
}