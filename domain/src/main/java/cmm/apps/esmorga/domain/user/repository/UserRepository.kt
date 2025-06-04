package cmm.apps.esmorga.domain.user.repository

import cmm.apps.esmorga.domain.user.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(name: String, lastName: String, email: String, password: String)
    suspend fun getUser(): User
    suspend fun emailVerification(email: String)
    suspend fun logout()
    suspend fun recoverPassword(email: String)
    suspend fun activateAccount(code: String)
    suspend fun resetPassword(code: String, password: String)
}