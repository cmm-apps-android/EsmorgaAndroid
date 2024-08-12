package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository


interface PerformRegistrationUserCase {
    suspend operator fun invoke(name: String, lastName: String, email: String, password: String): Result<Success<User>>
}

class PerformRegistrationUserCaseImpl(private val repo: UserRepository) : PerformRegistrationUserCase {
    override suspend fun invoke(name: String, lastName: String, email: String, password: String): Result<Success<User>>{
        try {
            val result = repo.register(name, lastName, email, password)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}