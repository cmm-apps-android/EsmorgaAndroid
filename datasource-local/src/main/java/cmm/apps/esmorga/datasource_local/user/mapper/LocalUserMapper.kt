package cmm.apps.esmorga.datasource_local.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel
import cmm.apps.esmorga.domain.user.model.RoleType

fun UserLocalModel.toUserDataModel(): UserDataModel {
    val parsedRoleType = RoleType.fromString(this.localRole)
    return UserDataModel(
        dataEmail = localEmail,
        dataName = localName,
        dataLastName = localLastName,
        dataRole = parsedRoleType
    )
}

fun UserDataModel.toUserLocalModel(): UserLocalModel = UserLocalModel(
    localEmail = dataEmail,
    localName = dataName,
    localLastName = dataLastName,
    localRole = dataRole.name
)