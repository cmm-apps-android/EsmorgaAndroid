package cmm.apps.esmorga.datasource_remote.auth

import android.content.SharedPreferences
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.user.AuthRemoteDatasourceImpl
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
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
        coEvery { sharedPreferences.edit().putString("access_token", capture(fakeTokenSlot)).apply() } coAnswers {
            fakeToken = fakeTokenSlot.captured
        }
        coEvery { sharedPreferences.edit().putString("refresh_token", capture(fakeRefreshTokenSlot)).apply() } coAnswers {
            fakeRefreshToken = fakeRefreshTokenSlot.captured
        }
        coEvery { sharedPreferences.edit().putLong("token_expiration_date", capture(fakeExpirationDateSlot)).apply() } coAnswers {
            fakeExpirationDate = fakeExpirationDateSlot.captured
        }
        coEvery {
            sharedPreferences.edit().run {
                remove("access_token")
                remove("refresh_token")
                remove("token_expiration_date")
            }.apply()
        } coAnswers {
            fakeToken = null
            fakeRefreshToken = null
            fakeExpirationDate = 0
        }

        return sharedPreferences
    }

    @Test
    fun `given a storage with access token when access token is cached then get access token stored successfully`() {
        val localAccessToken = "3883hh8fhfh84fh489"
        fakeToken = localAccessToken

        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences())
        sut.saveTokens(localAccessToken, localAccessToken, 0)
        val result = sut.getAccessToken()

        Assert.assertEquals(localAccessToken, result)
    }

    @Test
    fun `given a storage with refresh token when refresh token is cached then get refresh token stored successfully`() {
        val localRefreshToken = "3883hh8fhfh84fh489"
        fakeRefreshToken = localRefreshToken

        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences())
        sut.saveTokens(localRefreshToken, localRefreshToken, 0)
        val result = sut.getRefreshToken()

        Assert.assertEquals(localRefreshToken, result)
    }

    @Test
    fun `given a storage with access token, when delete tokens is invoked then access token is removed from storage`() {
        val localFakeToken = "3883hh8fhfh84fh489"
        fakeToken = localFakeToken

        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences())

        sut.deleteTokens()
        val finalResult = sut.getAccessToken()

        Assert.assertNull(finalResult)
    }

    @Test
    fun `given a storage with refresh token, when delete tokens is invoked then refresh token is removed from storage`() {
        val localFakeRefreshToken = "3883hh8fhfh84fh489"
        fakeRefreshToken = localFakeRefreshToken

        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences())

        sut.deleteTokens()
        val finalResult = sut.getRefreshToken()

        Assert.assertNull(finalResult)
    }

    @Test
    fun `given a storage with token expiration date, when delete tokens is invoked then token expiration date is removed from storage`() {
        val localExpirationDate = 3476347L
        fakeExpirationDate = localExpirationDate

        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val sut = AuthRemoteDatasourceImpl(api, provideFakeSharedPreferences())

        sut.deleteTokens()
        val finalResult = sut.getTokenExpirationDate()

        Assert.assertEquals(0, finalResult)
    }

}
