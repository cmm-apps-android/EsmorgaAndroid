package cmm.apps.esmorga.datasource_remote.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel

fun UserRemoteModel.toUserDataModel(): UserDataModel {
    return UserDataModel(
        nameData = profile.name,
        lastNameData = profile.lastName,
        emailData = profile.email
    )
}