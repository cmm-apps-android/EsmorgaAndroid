package cmm.apps.esmorga.datasource_local.user

import cmm.apps.esmorga.datasource_local.database.dao.UserDao
import cmm.apps.esmorga.datasource_local.mock.UserLocalMock
import cmm.apps.esmorga.datasource_local.user.mapper.toUserDataModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel
import cmm.apps.esmorga.domain.result.EsmorgaException
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test

class UserLocalDatasourceImplTest {
    private var fakeStorage: UserLocalModel? = null

    private fun provideFakeDao(): UserDao {
        val userSlot = slot<UserLocalModel>()
        val dao = mockk<UserDao>()
        coEvery { dao.getUser() } coAnswers {
            fakeStorage
        }
        coEvery { dao.insertUser(capture(userSlot)) } coAnswers {
            fakeStorage = userSlot.captured
        }
        coEvery { dao.deleteUser() } coAnswers {
            fakeStorage = null
        }

        return dao
    }

    @After
    fun shutDown() {
        fakeStorage = null
    }

    @Test
    fun `given a working dao when user requested then user successfully returned`() = runTest {
        val localUserName = "Draco"

        val dao = mockk<UserDao>(relaxed = true)
        coEvery { dao.getUser() } returns UserLocalMock.provideUser(name = localUserName)

        val sut = UserLocalDatasourceImpl(dao)
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given an empty storage when user cached then user is stored successfully`() = runTest {
        val localUserName = "Draco"

        val sut = UserLocalDatasourceImpl(provideFakeDao())
        sut.saveUser(UserLocalMock.provideUser(name = localUserName).toUserDataModel())
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given a storage with user when user cached then old user is removed and new user is stored successfully`() = runTest {
        val localUserName = "Draco"
        fakeStorage = UserLocalMock.provideUser()

        val sut = UserLocalDatasourceImpl(provideFakeDao())
        sut.saveUser(UserLocalMock.provideUser(name = localUserName).toUserDataModel())
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given a storage with user when is requested then is returned successfully`() = runTest {
        val localUserName = "Draco"
        fakeStorage = UserLocalMock.provideUser(name = localUserName)

        val sut = UserLocalDatasourceImpl(provideFakeDao())
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test(expected = EsmorgaException::class)
    fun `given an empty storage when is requested then correct error is returned`() = runTest {
        val sut = UserLocalDatasourceImpl(provideFakeDao())
        sut.getUser()
    }

}