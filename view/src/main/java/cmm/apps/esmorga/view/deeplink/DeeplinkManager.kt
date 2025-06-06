package cmm.apps.esmorga.view.deeplink

import android.net.Uri
import cmm.apps.esmorga.view.navigation.Navigation

const val DEEPLINK_ACTIVATE_ACCOUNT_SCREEN_NAME = "AccountActivationScreen"
const val DEEPLINK_RESET_PASSWORD_SCREEN_NAME = "ResetPasswordScreen"

const val DEEPLINK_ACTIVATE_ACCOUNT_QUERY_PARAM_NAME = "verificationCode"
const val DEEPLINK_RESET_PASSWORD_QUERY_PARAM_NAME = "forgotPasswordCode"

object DeeplinkManager {

    private fun deeplinkScreenName(deeplinkData: String): String {
        return when (deeplinkData) {
            DEEPLINK_ACTIVATE_ACCOUNT_QUERY_PARAM_NAME -> DEEPLINK_ACTIVATE_ACCOUNT_SCREEN_NAME
            DEEPLINK_RESET_PASSWORD_QUERY_PARAM_NAME -> DEEPLINK_RESET_PASSWORD_SCREEN_NAME
            else -> ""
        }
    }

    fun navigateFromDeeplink(deeplinkPath: Uri): Navigation {
        val deeplinkParameterName = deeplinkPath.queryParameterNames.first()
        val screenName = deeplinkScreenName(deeplinkParameterName)
        return when (screenName) {
            DEEPLINK_ACTIVATE_ACCOUNT_SCREEN_NAME -> Navigation.ActivateAccountScreen(deeplinkPath.getQueryParameter(deeplinkParameterName).orEmpty())
            DEEPLINK_RESET_PASSWORD_SCREEN_NAME -> Navigation.ResetPasswordScreen(deeplinkPath.getQueryParameter(deeplinkParameterName).orEmpty())
            else -> Navigation.WelcomeScreen
        }
    }
}