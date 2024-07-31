package cmm.apps.esmorga.domain.mock

import cmm.apps.esmorga.domain.user.model.User

object UserDomainMock {

    fun provideUser(name: String = "Ron", lastName: String = "Weasley", email: String = "ron@weasleyfamily.redhead") = User(
        name = name,
        lastName = lastName,
        email = email
    )
}