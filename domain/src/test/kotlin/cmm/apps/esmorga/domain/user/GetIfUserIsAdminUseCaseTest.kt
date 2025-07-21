package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.mock.UserDomainMock
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.RoleType
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetIfUserIsAdminUseCaseTest {

    private val getSavedUserUseCase: GetSavedUserUseCase = mockk()
    private lateinit var useCase: GetIfUserIsAdminUsecaseImpl

    @Before
    fun setUp() {
        useCase = GetIfUserIsAdminUsecaseImpl(getSavedUserUseCase)
    }

    @Test
    fun `given user is admin when usecase is invoked then return true`() = runTest {
        val user = UserDomainMock.provideUser(role = RoleType.ADMIN)
        coEvery { getSavedUserUseCase() } returns EsmorgaResult(user)

        val result = useCase()

        assertTrue(result)
    }

    @Test
    fun `given user is not admin when usecase is invoked then return false`() = runTest {
        val user = UserDomainMock.provideUser(role = RoleType.USER)
        coEvery { getSavedUserUseCase() } returns EsmorgaResult(user)

        val result = useCase()

        assertFalse(result)
    }
}
