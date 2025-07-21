package cmm.apps.esmorga.data.mock

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.domain.user.model.RoleType

object UserDataMock {

    fun provideUserDataModel(name: String = "Hermione", lastName: String = "Granger", email: String = "hermione@dirtyblood.com", role: RoleType = RoleType.USER): UserDataModel = UserDataModel(
        dataName = name,
        dataLastName = lastName,
        dataEmail = email,
        dataAccessToken = null,
        dataRefreshToken = null,
        dataRole = role
    )
}