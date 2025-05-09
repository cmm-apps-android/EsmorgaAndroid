package cmm.apps.esmorga.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics

object ConnectivityUtils {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
    }

    fun reportNoConnectivityIfNeeded(context: Context) {
        if (!isNetworkAvailable(context)) {
            FirebaseCrashlytics.getInstance().log("No connectivity during backend call")
            val esmorgaError = EsmorgaException(
                message = "No internet connection",
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

            FirebaseCrashlytics.getInstance().recordException(esmorgaError, keys)
        }
    }
}