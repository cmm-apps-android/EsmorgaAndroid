package cmm.apps.esmorga.datasource_remote.api.authenticator

import android.content.SharedPreferences
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_KEY
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.AUTHORIZATION_HEADER_VALUE
import cmm.apps.esmorga.datasource_remote.api.authenticator.AuthorizationConstants.SHARED_AUTH_TOKEN_KEY
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent

class EsmorgaAuthInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor, KoinComponent {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val accessToken = sharedPreferences.getString(SHARED_AUTH_TOKEN_KEY, null)
        accessToken?.let {
            requestBuilder.addHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE.format(it))
        }
        return chain.proceed(requestBuilder.build())
    }
}
