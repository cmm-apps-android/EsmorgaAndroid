package cmm.apps.esmorga.datasource_remote.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import com.google.firebase.crashlytics.FirebaseCrashlytics
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException


object ConnectionInterceptor : Interceptor, KoinComponent {

    val context: Context by inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!isNetworkAvailable(context)) {
            val errorMessage = "No connectivity during backend call"
            val exception = EsmorgaException(
                message = errorMessage,
                source = Source.REMOTE,
                code = ErrorCodes.NO_CONNECTION
            )
            FirebaseCrashlytics.getInstance().recordException(exception)
        }
        return chain.proceed(request)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}