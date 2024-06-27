package cmm.apps.esmorga.view.navigation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

sealed class Navigation(val route: String) {
    data object EventListScreen : Navigation("eventListScreen")
    data object EventDetailScreen : Navigation("eventDetailScreen/{eventId}") {
        fun createRoute(eventId: String) = "eventDetailScreen/$eventId"
    }

}

const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"

fun openNavigationApp(context: Context, latitude: Double, longitude: Double) {
    val uri = Uri.parse("geo:$latitude,$longitude")
    if (isPackageAvailable(context, GOOGLE_MAPS_PACKAGE)) {
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage(GOOGLE_MAPS_PACKAGE)
        context.startActivity(mapIntent)
    } else {
        // do nothing
    }
}

private fun isPackageAvailable(context: Context, appPackage: String) =
    try {
        val appInfo = context.packageManager?.getApplicationInfo(appPackage, 0)
        appInfo != null && appInfo.enabled
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }