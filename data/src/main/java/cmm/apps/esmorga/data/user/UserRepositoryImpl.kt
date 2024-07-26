package cmm.apps.esmorga.data.user

import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.mapper.toUser
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

class UserRepositoryImpl(private val localDs: UserDatasource, private val remoteDs: UserDatasource): UserRepository {
    override suspend fun login(email: String, password: String): Success<User> {
        try {
            val userResult = remoteDs.login(email, password)
            val userDataModel = userResult.getOrThrow()
            localDs.saveUser(userDataModel)
            return Success(userDataModel.toUser())
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUser(): Success<User> {
        try {
            val userDataModel = localDs.getUser()
            return Success(userDataModel.toUser())
        } catch (e: Exception) {
            throw e
        }
    }
}