package cmm.apps.esmorga.domain.account

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface ActivateAccountUseCase {
    suspend operator fun invoke(verificationCode: String): EsmorgaResult<User>
}

class ActivateAccountUseCaseImpl(
    private val repo: UserRepository
) : ActivateAccountUseCase {

    override suspend fun invoke(verificationCode: String): EsmorgaResult<User> {
         try {
            val result = repo.activateAccount(verificationCode)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}
