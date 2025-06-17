package cmm.apps.esmorga.datasource_remote.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


object ConnectionInterceptor : Interceptor, KoinComponent {

    val context: Context by inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!isNetworkAvailable(context)) {
            FirebaseCrashlytics.getInstance().log("No connectivity during backend call")
            val errorMessage = "No connectivity during backend call"
            val exception = EsmorgaException(
                message = errorMessage,
                source = Source.REMOTE,
                code = ErrorCodes.NO_CONNECTION
            )

            val extraDescription = if (isAirplaneModeOn(context)) "with having airplane mode activated" else "without having airplane mode activated"
            val keys = CustomKeysAndValues.Builder()
                .putString("Connection status", "offline")
                .putInt("Error Code", ErrorCodes.NO_CONNECTION)
                .putString("Error Domain", "com.esmorga")
                .putString("Error Description", "Loss of internet connection $extraDescription.")
                .putBoolean("Flight mode", isAirplaneModeOn(context))
                .build()

            FirebaseCrashlytics.getInstance().recordException(exception, keys)
        }
        return chain.proceed(request)
    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}