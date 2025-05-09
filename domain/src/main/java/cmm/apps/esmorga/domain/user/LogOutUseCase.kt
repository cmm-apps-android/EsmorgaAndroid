package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface LogOutUseCase {
    suspend operator fun invoke(): EsmorgaResult<Boolean>
}

class LogOutUseCaseImpl(private val repo: UserRepository) : LogOutUseCase {
    override suspend fun invoke(): EsmorgaResult<Boolean> {
        try {
            repo.logout()
            return EsmorgaResult(true)
        } catch (e: Exception) {
            return EsmorgaResult(false)
        }
    }
}