package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.domain.user.model.RoleType
import cmm.apps.esmorga.domain.user.model.User

object LoginViewMock {

    fun provideUser(name: String = "Minerva", lastname: String = "McGonagall", email: String = "mi_mcgonagall@hogwarts.edu", role: RoleType = RoleType.USER): User = User(name, lastname, email, role)
}