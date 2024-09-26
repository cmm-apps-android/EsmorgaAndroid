package cmm.apps.esmorga.datasource_remote.api.authenticator

import android.content.SharedPreferences
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_VALUE
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_REFRESH_TOKEN_KEY
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent

class EsmorgaAuthenticator(
    private val authDatasource: AuthDatasource,
    private val sharedPreferences: SharedPreferences
) : Authenticator, KoinComponent {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = sharedPreferences.getString(SHARED_REFRESH_TOKEN_KEY, null) ?: return null

        var newAccessToken: String?
        runBlocking {
            newAccessToken = authDatasource.refreshTokens(refreshToken)
        }
        return response.request.newBuilder()
            .header(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE.format(newAccessToken))
            .build()

    }
}