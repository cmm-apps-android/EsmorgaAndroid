package cmm.apps.esmorga.datasource_remote.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
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
        if (!isNetworkAvailable(context) && !isAirplaneModeOn(context)) {
            FirebaseCrashlytics.getInstance().log("No connectivity during backend call")
                FirebaseCrashlytics.getInstance()
                    .recordException(Exception("Connectivity lost unexpectedly"))
        }
    }
}
