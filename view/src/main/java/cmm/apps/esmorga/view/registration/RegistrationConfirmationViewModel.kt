package cmm.apps.esmorga.view.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.PerformRegistrationConfirmationUseCase
import cmm.apps.esmorga.view.registration.model.RegistrationConfirmationEffect
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class RegistrationConfirmationViewModel(val performRegistrationConfirmationUseCase: PerformRegistrationConfirmationUseCase) :
    ViewModel() {

    private val _effect: MutableSharedFlow<RegistrationConfirmationEffect> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<RegistrationConfirmationEffect> = _effect.asSharedFlow()

    fun onNavigateEmailApp() {
        _effect.tryEmit(RegistrationConfirmationEffect.NavigateToEmailApp)
    }

    fun onResendEmailClicked(email: String) {
        viewModelScope.launch {
            val result = performRegistrationConfirmationUseCase(email.trim())
            result.onSuccess {
                _effect.tryEmit(RegistrationConfirmationEffect.ShowSnackbarSuccess())
            }.onFailure {
                _effect.tryEmit(RegistrationConfirmationEffect.ShowSnackbarFailure())
            }
        }
    }
}