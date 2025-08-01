package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface PerformChangePasswordUseCase {
    suspend operator fun invoke(currentPassword: String, newPassword: String): EsmorgaResult<Unit>
}

class PerformChangePasswordUseCaseImpl(
    private val repo: UserRepository
) : PerformChangePasswordUseCase {
    override suspend fun invoke(currentPassword: String, newPassword: String): EsmorgaResult<Unit> {
        try {
            val result = repo.changePassword(currentPassword, newPassword)

            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }

    }
}