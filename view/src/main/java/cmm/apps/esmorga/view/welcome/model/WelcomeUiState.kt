package cmm.apps.esmorga.view.welcome.model

import android.content.Context
import cmm.apps.esmorga.view.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import cmm.apps.designsystem.R as DesignSystem
data class WelcomeUiState(
    val primaryButtonText: String = "",
    val secondaryButtonText: String = "",
    val icon: Int = DesignSystem.drawable.ic_logo,
    val deviceId: String? = null,
) : KoinComponent {
    fun createDefaultWelcomeUiState(): WelcomeUiState {
        val context: Context by inject()
        return WelcomeUiState(
            primaryButtonText = context.getString(R.string.button_login_register),
            secondaryButtonText = context.getString(R.string.button_guest),
            icon = DesignSystem.drawable.ic_logo,
            deviceId = deviceId
        )
    }
}

sealed class WelcomeEffect {
    data object NavigateToEventList : WelcomeEffect()
    data object NavigateToLogin : WelcomeEffect()
}