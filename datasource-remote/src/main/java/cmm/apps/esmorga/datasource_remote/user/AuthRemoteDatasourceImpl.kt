package cmm.apps.esmorga.datasource_remote.user

import android.content.SharedPreferences
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.REFRESH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_AUTH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_REFRESH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_TOKEN_EXPIRATION_DATE_KEY

class AuthRemoteDatasourceImpl(
    private val api: EsmorgaAuthApi,
    private val sharedPreferences: SharedPreferences
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

    override fun getAccessToken(): String? = sharedPreferences.getString(SHARED_AUTH_TOKEN_KEY, null)

    override fun getRefreshToken(): String? = sharedPreferences.getString(SHARED_REFRESH_TOKEN_KEY, null)

    override fun getTokenExpirationDate(): Long = sharedPreferences.getLong(SHARED_TOKEN_EXPIRATION_DATE_KEY, 0)

    override fun saveTokens(accessToken: String, refreshToken: String, ttl: Int) {
        sharedPreferences.edit().run {
            putString(SHARED_AUTH_TOKEN_KEY, accessToken)
            putString(SHARED_REFRESH_TOKEN_KEY, refreshToken)
            putLong(SHARED_TOKEN_EXPIRATION_DATE_KEY, System.currentTimeMillis() + ttl * 1000)
        }.apply()
    }

    override fun deleteTokens() {
        sharedPreferences.edit().run {
            remove(SHARED_AUTH_TOKEN_KEY)
            remove(SHARED_REFRESH_TOKEN_KEY)
            remove(SHARED_TOKEN_EXPIRATION_DATE_KEY)
        }.apply()
    }
}