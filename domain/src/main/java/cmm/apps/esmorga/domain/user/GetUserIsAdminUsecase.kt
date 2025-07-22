package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.user.model.RoleType

interface GetUserIsAdminUsecase{
    suspend operator fun invoke(): Boolean
}

class GetUserIsAdminUsecaseImpl(
    private val getSavedUserUseCase: GetSavedUserUseCase
) : GetUserIsAdminUsecase {

    override suspend fun invoke(): Boolean {
        val result = getSavedUserUseCase()
        val role = result.data?.role
        return role == RoleType.ADMIN
    }
}