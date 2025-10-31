package cmm.apps.esmorga.datasource_remote.api.authenticator

import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_VALUE
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent

class EsmorgaAuthInterceptor(
    private val authDatasource: AuthDatasource
) : Interceptor, KoinComponent {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val requestBuilder = chain.request().newBuilder()
        var accessToken = authDatasource.getAccessToken()
        val ttl = authDatasource.getTokenExpirationDate()
        if (ttl < System.currentTimeMillis()) {
            val refreshToken = authDatasource.getRefreshToken()
            refreshToken?.let {
                runBlocking {
                    accessToken = authDatasource.refreshTokens(refreshToken)
                }
            }
        }
        accessToken?.let {
            requestBuilder.addHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE.format(it))
        }
        chain.proceed(requestBuilder.build())
    }
}
