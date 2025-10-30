package cmm.apps.esmorga.datasource_remote.user

import android.content.SharedPreferences
import androidx.core.content.edit
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.REFRESH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_AUTH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_REFRESH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_TOKEN_EXPIRATION_DATE_KEY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRemoteDatasourceImpl(
    private val api: EsmorgaAuthApi,
    private val sharedPreferences: SharedPreferences,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthDatasource {
    override suspend fun refreshTokens(refreshToken: String): String? {
        return try {
            val refreshedTokens = api.refreshAccessToken(mapOf(REFRESH_TOKEN_KEY to refreshToken))
            saveTokens(
                refreshedTokens.remoteAccessToken,
                refreshedTokens.remoteRefreshToken,
                refreshedTokens.ttl
            )
            return refreshedTokens.remoteAccessToken
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAccessToken(): String? = withContext(coroutineDispatcher) {
        sharedPreferences.getString(SHARED_AUTH_TOKEN_KEY, null)
    }

    override suspend fun getRefreshToken(): String? = withContext(coroutineDispatcher) {
        sharedPreferences.getString(SHARED_REFRESH_TOKEN_KEY, null)
    }

    override suspend fun getTokenExpirationDate(): Long = withContext(coroutineDispatcher) {
        sharedPreferences.getLong(SHARED_TOKEN_EXPIRATION_DATE_KEY, 0)
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String, ttl: Int) {
        sharedPreferences.edit {
            putString(SHARED_AUTH_TOKEN_KEY, accessToken)
            putString(SHARED_REFRESH_TOKEN_KEY, refreshToken)
            putLong(SHARED_TOKEN_EXPIRATION_DATE_KEY, System.currentTimeMillis() + ttl * 1000)
        }
    }

    override suspend fun deleteTokens() {
        sharedPreferences.edit {
            remove(SHARED_AUTH_TOKEN_KEY)
            remove(SHARED_REFRESH_TOKEN_KEY)
            remove(SHARED_TOKEN_EXPIRATION_DATE_KEY)
        }
    }
}