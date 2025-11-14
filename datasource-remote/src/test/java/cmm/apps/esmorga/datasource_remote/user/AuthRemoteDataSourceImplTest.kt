package cmm.apps.esmorga.datasource_remote.user

import android.content.SharedPreferences
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAccountApi
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class AuthRemoteDataSourceImplTest {
    private var fakeToken: String? = null
    private var fakeRefreshToken: String? = null
    private var fakeExpirationDate: Long = 0

    private fun provideFakeSharedPreferences(): SharedPreferences {
        val fakeTokenSlot = slot<String>()
        val fakeRefreshTokenSlot = slot<String>()
        val fakeExpirationDateSlot = slot<Long>()
        val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        coEvery { sharedPreferences.getString("access_token", null) } coAnswers {
            fakeToken
        }
        coEvery { sharedPreferences.getString("refresh_token", null) } coAnswers {
            fakeRefreshToken
        }
        coEvery { sharedPreferences.getLong("token_expiration_date", 0) } coAnswers {
            fakeExpirationDate
        }
        coEvery { sharedPreferences.edit().putString("access_token", capture(fakeTokenSlot)) } coAnswers {
            fakeToken = fakeTokenSlot.captured
            sharedPreferences.edit()
        }
        coEvery { sharedPreferences.edit().putString("refresh_token", capture(fakeRefreshTokenSlot)) } coAnswers {
            fakeRefreshToken = fakeRefreshTokenSlot.captured
            sharedPreferences.edit()
        }
        coEvery { sharedPreferences.edit().putLong("token_expiration_date", capture(fakeExpirationDateSlot)) } coAnswers {
            fakeExpirationDate = fakeExpirationDateSlot.captured
            sharedPreferences.edit()
        }
        coEvery { sharedPreferences.edit().remove("access_token") } coAnswers {
            fakeToken = null
            sharedPreferences.edit()
        }
        coEvery { sharedPreferences.edit().remove("refresh_token") } coAnswers {
            fakeRefreshToken = null
            sharedPreferences.edit()
        }
        coEvery { sharedPreferences.edit().remove("token_expiration_date") } coAnswers {
            fakeExpirationDate = 0
            sharedPreferences.edit()
        }

        return sharedPreferences
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given a storage with access token when access token is cached then get access token stored successfully`() = runTest {
        val localAccessToken = "localAccessToken"
        fakeToken = ""

        val api = mockk<EsmorgaAccountApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences(), UnconfinedTestDispatcher())
        sut.saveTokens(localAccessToken, "", 0)
        val result = sut.getAccessToken()

        Assert.assertEquals(localAccessToken, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given a storage with refresh token when refresh token is cached then get refresh token stored successfully`() = runTest {
        val localRefreshToken = "localRefreshToken"
        fakeRefreshToken = ""

        val api = mockk<EsmorgaAccountApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences(), UnconfinedTestDispatcher())
        sut.saveTokens("", localRefreshToken, 0)
        val result = sut.getRefreshToken()

        Assert.assertEquals(localRefreshToken, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given a storage with access token, when delete tokens is invoked then access token is removed from storage`() = runTest {
        val localFakeToken = "localFakeToken"
        fakeToken = localFakeToken

        val api = mockk<EsmorgaAccountApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences(), UnconfinedTestDispatcher())

        sut.deleteTokens()
        val finalResult = sut.getAccessToken()

        Assert.assertNull(finalResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given a storage with refresh token, when delete tokens is invoked then refresh token is removed from storage`() = runTest {
        val localFakeRefreshToken = "localFakeRefreshToken"
        fakeRefreshToken = localFakeRefreshToken

        val api = mockk<EsmorgaAccountApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences(), UnconfinedTestDispatcher())

        sut.deleteTokens()
        val finalResult = sut.getRefreshToken()

        Assert.assertNull(finalResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given a storage with token expiration date, when delete tokens is invoked then token expiration date is removed from storage`() = runTest {
        val localExpirationDate = 3476347L
        fakeExpirationDate = localExpirationDate

        val api = mockk<EsmorgaAccountApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences(), UnconfinedTestDispatcher())

        sut.deleteTokens()
        val finalResult = sut.getTokenExpirationDate()

        Assert.assertEquals(0, finalResult)
    }

}