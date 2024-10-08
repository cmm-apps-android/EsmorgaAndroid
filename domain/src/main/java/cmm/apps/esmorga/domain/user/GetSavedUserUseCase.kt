package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface GetSavedUserUseCase {
    suspend operator fun invoke(): EsmorgaResult<User>
}

class GetSavedUserUseCaseImpl(private val repo: UserRepository) : GetSavedUserUseCase {
    override suspend fun invoke(): EsmorgaResult<User> {
        try {
            val result = repo.getUser()
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}