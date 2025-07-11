package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.domain.user.model.User

object LoginViewMock {

    fun provideUser(name: String = "Minerva", lastname: String = "McGonagall", email: String = "mi_mcgonagall@hogwarts.edu", role: String = "USER"): User = User(name, lastname, email, role)
}