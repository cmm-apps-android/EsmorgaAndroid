package cmm.apps.esmorga.datasource_remote.api.authenticator

import android.content.Context
import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_VALUE
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.REFRESH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_AUTH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_PREFERENCES_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_REFRESH_TOKEN_KEY
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EsmorgaAuthenticator : Authenticator, KoinComponent {

    private val context: Context by inject()
    private val apiService: EsmorgaApi by inject()
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = sharedPreferences.getString(SHARED_REFRESH_TOKEN_KEY, null) ?: return null

        var newAccessToken: String?
        runBlocking {
            newAccessToken = refreshAccessToken(refreshToken)
        }
        return response.request.newBuilder()
            .header(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE.format(newAccessToken))
            .build()

    }

    private suspend fun refreshAccessToken(refreshToken: String): String? {
        return try {
            val refreshedTokens = apiService.refreshAccessToken(mapOf(REFRESH_TOKEN_KEY to refreshToken))
            sharedPreferences.edit().putString(SHARED_REFRESH_TOKEN_KEY, refreshedTokens.remoteRefreshToken).apply()
            sharedPreferences.edit().putString(SHARED_AUTH_TOKEN_KEY, refreshedTokens.remoteAccessToken).apply()
            return refreshedTokens.remoteAccessToken
        } catch (e: Exception) {
            null
        }
    }
}