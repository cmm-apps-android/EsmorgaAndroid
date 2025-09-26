package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface LogOutUseCaseV2{
    suspend operator fun invoke(): EsmorgaResult<Boolean>
}

class LogOutUseCaseV2Impl(
    private val repo: UserRepository
) : LogOutUseCaseV2{
    override suspend fun invoke(): EsmorgaResult<Boolean> {
        try {
            repo.logout()
            return EsmorgaResult(true)
        } catch (e: Exception) {
            return EsmorgaResult(false)
        }
    }

}