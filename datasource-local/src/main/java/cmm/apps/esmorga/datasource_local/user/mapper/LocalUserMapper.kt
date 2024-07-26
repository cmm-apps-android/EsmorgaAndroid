package cmm.apps.esmorga.datasource_local.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel

fun UserLocalModel.toUserDataModel(): UserDataModel = UserDataModel(
    email = email,
    name = name,
    lastName = lastName
)

fun UserDataModel.toUserLocalModel(): UserLocalModel = UserLocalModel(
    email = email,
    name = name,
    lastName = lastName
)