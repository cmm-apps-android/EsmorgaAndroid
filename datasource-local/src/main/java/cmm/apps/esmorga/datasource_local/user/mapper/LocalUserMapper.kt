package cmm.apps.esmorga.datasource_local.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel

fun UserLocalModel.toUserDataModel(accessToken: String?, refreshToken: String?): UserDataModel = UserDataModel(
    dataEmail = localEmail,
    dataName = localName,
    dataLastName = localLastName,
    dataAccessToken = accessToken,
    dataRefreshToken = refreshToken
)

fun UserDataModel.toUserLocalModel(): UserLocalModel = UserLocalModel(
    localEmail = dataEmail,
    localName = dataName,
    localLastName = dataLastName
)