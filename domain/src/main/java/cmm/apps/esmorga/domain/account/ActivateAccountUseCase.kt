package cmm.apps.esmorga.domain.account

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface ActivateAccountUseCase {
    suspend operator fun invoke(verificationCode: String): EsmorgaResult<Unit>
}

class ActivateAccountUseCaseImpl(
    private val repo: UserRepository
) : ActivateAccountUseCase {

    override suspend fun invoke(verificationCode: String): EsmorgaResult<Unit> {
        return try {
            repo.activateAccount(verificationCode)
            EsmorgaResult.success(Unit)
        } catch (e: Exception) {
            EsmorgaResult.failure(e)
        }
    }
}
