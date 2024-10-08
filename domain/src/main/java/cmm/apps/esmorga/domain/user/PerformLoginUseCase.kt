package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface PerformLoginUseCase {
    suspend operator fun invoke(email: String, password: String): EsmorgaResult<User>
}

class PerformLoginUseCaseImpl(private val repo: UserRepository) : PerformLoginUseCase {
    override suspend fun invoke(email: String, password: String): EsmorgaResult<User> {
        try {
            val result = repo.login(email, password)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}