package cmm.apps.esmorga.datasource_remote.api.authenticator

import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_VALUE
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent

class EsmorgaAuthenticator(
    private val authDatasource: AuthDatasource
) : Authenticator, KoinComponent {

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val refreshToken = authDatasource.getRefreshToken()

        refreshToken?.let {
            val newAccessToken = authDatasource.refreshTokens(it)
            response.request.newBuilder()
                .header(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE.format(newAccessToken))
                .build()
        }
    }
}