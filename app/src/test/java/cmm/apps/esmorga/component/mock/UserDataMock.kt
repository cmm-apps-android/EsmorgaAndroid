package cmm.apps.esmorga.component.mock

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.domain.user.model.RoleType


object UserDataMock {

    fun provideUserDataModel(name: String = "Kame", lastName: String = "Sennin", email: String = "kamesennin@db.com", role: RoleType = RoleType.USER): UserDataModel = UserDataModel(
        dataName = name,
        dataLastName = lastName,
        dataEmail = email,
        dataAccessToken = null,
        dataRefreshToken = null,
        dataRole = role
    )

}