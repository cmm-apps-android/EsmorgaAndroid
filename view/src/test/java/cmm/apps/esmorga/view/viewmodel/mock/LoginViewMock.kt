package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.domain.user.model.User

object LoginViewMock {

    fun provideUser(name: String = "Minerva", surname: String = "McGonagall", email: String = "mi_mcgonagall@hogwarts.edu"): User = User(name, surname, email)
}