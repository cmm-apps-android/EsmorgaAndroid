package cmm.apps.esmorga.domain.user.repository

import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): Success<User>
    suspend fun register(name: String, lastName: String, email: String, password: String): Success<User>
    suspend fun getUser(): Success<User>
}