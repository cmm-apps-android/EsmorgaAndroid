package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface PerformRegistrationUserCase {
    suspend operator fun invoke(name: String, lastName: String, email: String, password: String): EsmorgaResult<Unit>
}

class PerformRegistrationUserCaseImpl(private val repo: UserRepository) : PerformRegistrationUserCase {
    override suspend fun invoke(name: String, lastName: String, email: String, password: String): EsmorgaResult<Unit> {
        try {
            val result = repo.register(name, lastName, email, password)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}