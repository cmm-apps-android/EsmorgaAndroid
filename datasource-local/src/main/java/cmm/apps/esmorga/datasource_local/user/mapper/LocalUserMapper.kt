package cmm.apps.esmorga.datasource_local.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel

fun UserLocalModel.toUserDataModel(): UserDataModel = UserDataModel(
    emailData = emailLocal,
    nameData = nameLocal,
    lastNameData = lastNameLocal
)

fun UserDataModel.toUserLocalModel(): UserLocalModel = UserLocalModel(
    emailLocal = emailData,
    nameLocal = nameData,
    lastNameLocal = lastNameData
)