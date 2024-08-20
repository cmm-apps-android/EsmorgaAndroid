package cmm.apps.esmorga.view.navigation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import kotlinx.serialization.Serializable

sealed class Navigation {

    @Serializable
    data object WelcomeScreen : Navigation()

    @Serializable
    data object EventListScreen : Navigation()

    @Serializable
    data class EventDetailScreen(val eventId: String) : Navigation()

    @Serializable
    data object LoginScreen : Navigation()

    @Serializable
    data object RegistrationScreen : Navigation()

    @Serializable
    data class FullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments) : Navigation()
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