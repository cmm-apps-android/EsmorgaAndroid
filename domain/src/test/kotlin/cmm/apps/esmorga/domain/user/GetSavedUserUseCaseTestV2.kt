package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.user.model.RoleType
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetSavedUserUseCaseTestV2 {

    @Test
    fun `given existing saved user when retrieving user then return saved user`() = runTest {
        val repo = mockk<UserRepository>()
        val expectedUser = User("Name", "LastName", "Email", RoleType.ADMIN)
        coEvery { repo.getUser() } returns expectedUser

        val sut = GetSavedUserUseCaseV2Impl(repo)
        val result = sut.invoke()

        Assert.assertNotNull(result.data)
        Assert.assertEquals(expectedUser, result.data)
        Assert.assertNull(result.error)
    }

    @Test
    fun `given error when retrieving saved user then return failure`() = runTest {
        val repo = mockk<UserRepository>()
        val exception = RuntimeException("DB error")
        coEvery { repo.getUser() } throws exception

        val sut = GetSavedUserUseCaseV2Impl(repo)

        val result = sut.invoke()

        Assert.assertNull(result.data)
        Assert.assertNotNull(result.error)
        Assert.assertEquals("Error:", result.error?.message)
    }
}