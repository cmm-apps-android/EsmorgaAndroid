package cmm.apps.esmorga.view.deeplink

import android.net.Uri
import cmm.apps.esmorga.view.navigation.Navigation
import org.junit.Assert
import org.junit.Test

class DeeplinkManagerTest {

    @Test
    fun `given a deeplink, when click on activate account deeplink, navigates to activate account screen`() {
        val verificationCode = "38463"
        val deeplink = "https://esmorga-app/activate-account?verificationCode=38463"
        val result = DeeplinkManager.navigateFromDeeplink(Uri.parse(deeplink))

        Assert.assertEquals(Navigation.ActivateAccountScreen(verificationCode), result)
    }

    @Test
    fun `given a deeplink, when click on forgot password deeplink, navigates to forgot password screen`() {
        val forgotPassCode = "38463"
        val deeplink = "https://esmorga-app/forgot-password?forgotPasswordCode=38463"
        val result = DeeplinkManager.navigateFromDeeplink(Uri.parse(deeplink))

        Assert.assertEquals(Navigation.ResetPasswordScreen(forgotPassCode), result)
    }

}