package cmm.apps.esmorga.datasource_remote.mock

import android.content.SharedPreferences
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthInterceptor
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthenticator
import io.mockk.mockk

object EsmorgaAuthenticationMock {
    fun getEsmorgaAuthenticatorMock() : EsmorgaAuthenticator{
        val esmorgaAuthenticatorMock = mockk<EsmorgaAuthenticator>(relaxed = true)
        return esmorgaAuthenticatorMock
    }

    fun getAuthInterceptor() : EsmorgaAuthInterceptor {
        val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
        return EsmorgaAuthInterceptor(mockSharedPreferences)
    }
}