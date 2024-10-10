package cmm.apps.esmorga.datasource_remote.mock

import android.content.SharedPreferences
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthInterceptor
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthenticator
import io.mockk.mockk

object EsmorgaAuthenticationMock {
    fun getEsmorgaAuthenticatorMock() : EsmorgaAuthenticator{
        val esmorgaAuthenticatorMock = mockk<EsmorgaAuthenticator>(relaxed = true)
        return esmorgaAuthenticatorMock
    }

    fun getAuthInterceptor() : EsmorgaAuthInterceptor {
        val mockAuthDatasource = mockk<AuthDatasource>(relaxed = true)
        val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
        return EsmorgaAuthInterceptor(mockAuthDatasource, mockSharedPreferences)
    }
}