package cmm.apps.esmorga.view.deeplink

import android.net.Uri
import cmm.apps.esmorga.view.navigation.Navigation

const val DEEPLINK_ACTIVATE_ACCOUNT_QUERY_PARAM_NAME = "verificationCode"
const val DEEPLINK_ACTIVATE_ACCOUNT_SCREEN_NAME = "AccountActivationScreen"

object DeeplinkManager {

    private fun deeplinkScreenName(deeplinkData: String): String {
        return when (deeplinkData) {
            DEEPLINK_ACTIVATE_ACCOUNT_QUERY_PARAM_NAME -> DEEPLINK_ACTIVATE_ACCOUNT_SCREEN_NAME
            else -> ""
        }
    }

    fun navigateFromDeeplink(deeplinkPath: Uri): Navigation {
        val deeplinkData = deeplinkScreenName(deeplinkPath.queryParameterNames.first())
        return when (deeplinkData) {
            DEEPLINK_ACTIVATE_ACCOUNT_SCREEN_NAME -> Navigation.ActivateAccountScreen(deeplinkData)
            else -> Navigation.WelcomeScreen
        }
    }
}