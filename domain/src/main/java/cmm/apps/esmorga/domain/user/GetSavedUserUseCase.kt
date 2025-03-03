package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface GetSavedUserUseCase {
    suspend operator fun invoke(): EsmorgaResult<User>
    suspend fun clearUser()
}

class GetSavedUserUseCaseImpl(private val repo: UserRepository) : GetSavedUserUseCase {
    override suspend fun invoke(): EsmorgaResult<User> {
        return try {
            val result = repo.getUser()
            EsmorgaResult.success(result)
        } catch (e: Exception) {
            EsmorgaResult.failure(e)
        }
    }

    override suspend fun clearUser() {
        try {
            repo.logout()
        } catch (e: Exception) {
        }
    }
}
