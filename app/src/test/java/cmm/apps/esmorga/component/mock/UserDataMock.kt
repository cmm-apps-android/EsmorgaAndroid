package cmm.apps.esmorga.component.mock

import cmm.apps.esmorga.data.user.model.UserDataModel


object UserDataMock {

    fun provideUserDataModel(name: String = "Kame", lastName: String = "Sennin", email: String = "kamesennin@db.com", role: String = "USER"): UserDataModel = UserDataModel(
        dataName = name,
        dataLastName = lastName,
        dataEmail = email,
        dataAccessToken = null,
        dataRefreshToken = null,
        dataRole = role,
        dataTtl = 600
    )

}