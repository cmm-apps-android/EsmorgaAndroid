package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.user.model.RoleType

interface GetIfUserIsAdminUsecase{
    suspend operator fun invoke(): Boolean
}

class GetIfUserIsAdminUsecaseImpl(
    private val getSavedUserUseCase: GetSavedUserUseCase
) : GetIfUserIsAdminUsecase {

    override suspend fun invoke(): Boolean {
        val result = getSavedUserUseCase()
        val role = result.data?.role
        return role == RoleType.ADMIN
    }
}