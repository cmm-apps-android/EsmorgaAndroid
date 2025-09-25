package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface GetSavedUserUseCaseV2{
    suspend operator fun invoke(): EsmorgaResult<User>
}

class GetSavedUserUseCaseV2Impl(private val repo:UserRepository) : GetSavedUserUseCaseV2 {
    override suspend fun invoke(): EsmorgaResult<User> {
        try {
            val result = repo.getUser()
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}