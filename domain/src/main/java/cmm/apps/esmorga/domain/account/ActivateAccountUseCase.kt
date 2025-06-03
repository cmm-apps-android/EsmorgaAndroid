package cmm.apps.esmorga.domain.account

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface ActivateAccountUseCase {
    suspend operator fun invoke(code: String): EsmorgaResult<Unit>
}

class ActivateAccountUseCaseImpl(
    private val repo: UserRepository
) : ActivateAccountUseCase {

    override suspend fun invoke(code: String): EsmorgaResult<Unit> {
        return try {
            repo.activateAccount(code)
            EsmorgaResult.success(Unit)
        } catch (e: Exception) {
            EsmorgaResult.failure(e)
        }
    }
}
