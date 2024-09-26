package cmm.apps.esmorga.datasource_remote.user

import android.content.SharedPreferences
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.REFRESH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_AUTH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_REFRESH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_TTL

class AuthRemoteDatasourceImpl(
    private val api: EsmorgaAuthApi,
    private val sharedPreferences: SharedPreferences
) : AuthDatasource {
    override suspend fun refreshTokens(refreshToken: String): String? {
        return try {
            val refreshedTokens = api.refreshAccessToken(mapOf(REFRESH_TOKEN_KEY to refreshToken))
            sharedPreferences.edit().run {
                putString(SHARED_REFRESH_TOKEN_KEY, refreshedTokens.remoteRefreshToken)
                putString(SHARED_AUTH_TOKEN_KEY, refreshedTokens.remoteAccessToken)
                putLong(SHARED_TTL, System.currentTimeMillis() + refreshedTokens.ttl * 1000)
            }.apply()
            return refreshedTokens.remoteAccessToken
        } catch (e: Exception) {
            null
        }
    }
}