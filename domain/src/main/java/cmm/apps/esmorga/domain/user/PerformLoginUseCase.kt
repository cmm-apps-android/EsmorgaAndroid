package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface PerformLoginUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Success<User>>
}

class PerformLoginUseCaseImpl(private val repo: UserRepository) : PerformLoginUseCase {
    override suspend fun invoke(email: String, password: String): Result<Success<User>> {
        try {
            val result = repo.login(email, password)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}