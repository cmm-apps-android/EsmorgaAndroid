package cmm.apps.esmorga.data.user

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.mapper.toUser
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

class UserRepositoryImpl(
    private val localDs: UserDatasource,
    private val remoteDs: UserDatasource,
    private val localEventDs: EventDatasource,
    private val localPollsDs: PollDatasource
) : UserRepository {
    override suspend fun login(email: String, password: String): User {
        val userDataModel = remoteDs.login(email, password)
        localDs.saveUser(userDataModel)
        localEventDs.deleteCacheEvents()
        localPollsDs.deleteCachePolls()
        return userDataModel.toUser()
    }

    override suspend fun register(name: String, lastName: String, email: String, password: String) {
        remoteDs.register(name, lastName, email, password)
    }

    override suspend fun getUser(): User {
        val userDataModel = localDs.getUser()
        return userDataModel.toUser()
    }

    override suspend fun emailVerification(email: String) {
        remoteDs.emailVerification(email)
    }

    override suspend fun logout() {
        try {
            localDs.deleteUserSession()
            remoteDs.deleteUserSession()
            localEventDs.deleteCacheEvents()
            localPollsDs.deleteCachePolls()
        } catch (e: Exception) {
            throw Exception("Error al cerrar sesión: ${e.message}", e)
        }
    }

    override suspend fun recoverPassword(email: String) {
        remoteDs.recoverPassword(email)
    }

    override suspend fun activateAccount(verificationCode: String) {
        val userDataModel = remoteDs.activateAccount(verificationCode)
        localDs.saveUser(userDataModel)
        localEventDs.deleteCacheEvents()
        localPollsDs.deleteCachePolls()
    }

    override suspend fun resetPassword(code: String, password: String) {
        remoteDs.resetPassword(code, password)
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String) {
        remoteDs.changePassword(currentPassword, newPassword)
    }
}