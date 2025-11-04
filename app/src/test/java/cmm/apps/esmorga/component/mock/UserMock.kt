package cmm.apps.esmorga.component.mock

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel
import cmm.apps.esmorga.domain.user.model.RoleType
import cmm.apps.esmorga.domain.user.model.User


object UserMock {

    fun provideUserLocalModel(name: String = "Kame", lastName: String = "Sennin", email: String = "kamesennin@db.com", role: RoleType = RoleType.USER): UserLocalModel = UserLocalModel(
        localName = name,
        localLastName = lastName,
        localEmail = email,
        localRole = role.toString()
    )

}