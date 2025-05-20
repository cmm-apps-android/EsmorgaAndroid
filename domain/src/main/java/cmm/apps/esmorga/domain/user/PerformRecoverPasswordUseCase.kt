package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface PerformRecoverPasswordUseCase {
    suspend operator fun invoke(email: String): EsmorgaResult<Unit>
}

class PerformRecoverPasswordUseCaseImpl(private val repo: UserRepository) :
    PerformRecoverPasswordUseCase {
    override suspend fun invoke(email: String): EsmorgaResult<Unit> {
        try {
            val result = repo.recoverPassword(email)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}