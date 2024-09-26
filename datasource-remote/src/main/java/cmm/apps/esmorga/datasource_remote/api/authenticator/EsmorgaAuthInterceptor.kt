package cmm.apps.esmorga.datasource_remote.api.authenticator

import android.content.SharedPreferences
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_VALUE
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_AUTH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_REFRESH_TOKEN_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_TTL
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent

class EsmorgaAuthInterceptor(
    private val authDatasource: AuthDatasource,
    private val sharedPreferences: SharedPreferences
) : Interceptor, KoinComponent {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        var accessToken = sharedPreferences.getString(SHARED_AUTH_TOKEN_KEY, null)
        val ttl = sharedPreferences.getLong(SHARED_TTL, 0)
        if (ttl < System.currentTimeMillis()) {
            val refreshToken = sharedPreferences.getString(SHARED_REFRESH_TOKEN_KEY, null)
            refreshToken?.let {
                runBlocking {
                    accessToken = authDatasource.refreshTokens(refreshToken)
                }
            }
        }
        accessToken?.let {
            requestBuilder.addHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE.format(it))
        }
        return chain.proceed(requestBuilder.build())
    }
}
