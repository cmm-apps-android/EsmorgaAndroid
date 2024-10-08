package cmm.apps.esmorga.domain.user.repository

import cmm.apps.esmorga.domain.user.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(name: String, lastName: String, email: String, password: String): User
    suspend fun getUser(): User
}