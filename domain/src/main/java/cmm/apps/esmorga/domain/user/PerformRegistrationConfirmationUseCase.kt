package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface PerformRegistrationConfirmationUseCase {
    suspend operator fun invoke(email: String): EsmorgaResult<Unit>
}

class PerformRegistrationConfirmationUseCaseImpl(private val repo: UserRepository) :
    PerformRegistrationConfirmationUseCase {
    override suspend fun invoke(email: String): EsmorgaResult<Unit> {
        try {
            val result = repo.emailVerification(email)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}