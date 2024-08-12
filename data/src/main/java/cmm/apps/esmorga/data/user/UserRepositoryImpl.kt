package cmm.apps.esmorga.data.user

import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.mapper.toUser
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

class UserRepositoryImpl(private val localDs: UserDatasource, private val remoteDs: UserDatasource) : UserRepository {
    override suspend fun login(email: String, password: String): Success<User> {
        val userDataModel = remoteDs.login(email, password)
        localDs.saveUser(userDataModel)
        return Success(userDataModel.toUser())
    }

    override suspend fun register(name: String, lastName: String, email: String, password: String): Success<User> {
        val userDataModel = remoteDs.register(name, lastName, email, password)
        localDs.saveUser(userDataModel)
        return Success(userDataModel.toUser())
    }

    override suspend fun getUser(): Success<User> {
        val userDataModel = localDs.getUser()
        return Success(userDataModel.toUser())
    }
}