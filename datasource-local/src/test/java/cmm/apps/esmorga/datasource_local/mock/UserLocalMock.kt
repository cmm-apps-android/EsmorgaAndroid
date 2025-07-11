package cmm.apps.esmorga.datasource_local.mock

import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel

object UserLocalMock {

    fun provideUser(email: String = "severus-snape@hogwarts.edu", name: String = "Severus", lastName: String = "Snape", role: String = "USER") = UserLocalModel(
        localEmail = email,
        localName = name,
        localLastName = lastName,
        localRole = role
    )
}