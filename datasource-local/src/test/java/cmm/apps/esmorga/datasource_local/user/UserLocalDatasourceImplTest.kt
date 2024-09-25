package cmm.apps.esmorga.datasource_local.user

import android.content.SharedPreferences
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
    private var fakeSharedToken: String? = null
    private var fakeSharedRefreshToken: String? = null

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

    private fun provideFakeSharedPreferences() : SharedPreferences {
        val fakeSharedTokenSlot = slot<String>()
        val fakeSharedRefreshTokenSlot = slot<String>()
        val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        coEvery { sharedPreferences.getString("refresh_token", null) } coAnswers {
            fakeSharedToken
        }
        coEvery { sharedPreferences.getString("access_token", null) } coAnswers {
            fakeSharedRefreshToken
        }
        coEvery { sharedPreferences.edit().putString("access_token", capture(fakeSharedTokenSlot)).apply() } coAnswers {
            fakeSharedToken = fakeSharedTokenSlot.captured
        }
        coEvery { sharedPreferences.edit().putString("refresh_token", capture(fakeSharedRefreshTokenSlot)).apply() } coAnswers {
            fakeSharedRefreshToken = fakeSharedRefreshTokenSlot.captured
        }
        return sharedPreferences
    }

    @After
    fun shutDown() {
        fakeStorage = null
    }

    @Test
    fun `given a working dao when user requested then user successfully returned`() = runTest {
        val localUserName = "Draco"

        val dao = mockk<UserDao>(relaxed = true)
        val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        coEvery { dao.getUser() } returns UserLocalMock.provideUser(name = localUserName)

        val sut = UserLocalDatasourceImpl(dao, sharedPreferences)
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given an empty storage when user cached then user is stored successfully`() = runTest {
        val localUserName = "Draco"

        val sut = UserLocalDatasourceImpl(provideFakeDao(), provideFakeSharedPreferences())
        sut.saveUser(UserLocalMock.provideUser(name = localUserName).toUserDataModel(null, null))
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given a storage with user when user cached then old user is removed and new user is stored successfully`() = runTest {
        val localUserName = "Draco"
        fakeStorage = UserLocalMock.provideUser()

        val sut = UserLocalDatasourceImpl(provideFakeDao(), provideFakeSharedPreferences())
        sut.saveUser(UserLocalMock.provideUser(name = localUserName).toUserDataModel(null, null))
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test
    fun `given a storage with user when is requested then is returned successfully`() = runTest {
        val localUserName = "Draco"
        fakeStorage = UserLocalMock.provideUser(name = localUserName)

        val sut = UserLocalDatasourceImpl(provideFakeDao(), provideFakeSharedPreferences())
        val result = sut.getUser()

        Assert.assertEquals(localUserName, result.dataName)
    }

    @Test(expected = EsmorgaException::class)
    fun `given an empty storage when is requested then correct error is returned`() = runTest {
        val sut = UserLocalDatasourceImpl(provideFakeDao(), provideFakeSharedPreferences())
        sut.getUser()
    }

}