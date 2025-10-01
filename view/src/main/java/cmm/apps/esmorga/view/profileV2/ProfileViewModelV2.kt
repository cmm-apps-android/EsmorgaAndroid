package cmm.apps.esmorga.view.profileV2

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.GetSavedUserUseCaseV2
import cmm.apps.esmorga.domain.user.LogOutUseCaseV2
import cmm.apps.esmorga.view.profileV2.model.ProfileEffectV2
import cmm.apps.esmorga.view.profileV2.model.ProfileUiStateV2
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModelV2(
    private val getSavedUserCaseV2: GetSavedUserUseCaseV2,
    private val logOutUseCaseV2UseCase: LogOutUseCaseV2
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(ProfileUiStateV2())
    val uiState: StateFlow<ProfileUiStateV2> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<ProfileEffectV2> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<ProfileEffectV2> = _effect.asSharedFlow()

    init {
        showUser()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        showUser()
    }

    fun showUser() {
        viewModelScope.launch {
            getSavedUserCaseV2.invoke()
                .onSuccess { user ->
                    _uiState.value = ProfileUiStateV2(user = user)
                }
                .onFailure {
                    _uiState.value = ProfileUiStateV2(user = null)
                }
        }
    }

    fun logOut(){
        viewModelScope.launch {
            logOutUseCaseV2UseCase.invoke()
            _uiState.value = ProfileUiStateV2(user = null)

        }
    }
    fun navigateTologIn() {
        _effect.tryEmit(ProfileEffectV2.NavigateToLogIn)
    }

    fun navigateToChangePassword() {
        _effect.tryEmit(ProfileEffectV2.NavigateToChangePassword)
    }

}