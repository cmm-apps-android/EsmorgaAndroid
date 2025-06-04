package cmm.apps.esmorga.domain.user.repository

import cmm.apps.esmorga.domain.result.EsmorgaResult

interface PerformResetPasswordUseCase {
    suspend operator fun invoke(code: String, password: String): EsmorgaResult<Unit>
}

class PerformResetPasswordUseCaseImpl(private val repo: UserRepository) : PerformResetPasswordUseCase {
    override suspend fun invoke(code: String, password: String): EsmorgaResult<Unit> {
        try {
            val result = repo.resetPassword(code, password)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}