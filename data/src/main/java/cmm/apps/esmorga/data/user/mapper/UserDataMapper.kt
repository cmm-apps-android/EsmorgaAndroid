package cmm.apps.esmorga.data.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.domain.user.model.User

fun UserDataModel.toUser() = User(
    name = nameData,
    lastName = lastNameData,
    email = emailData
)