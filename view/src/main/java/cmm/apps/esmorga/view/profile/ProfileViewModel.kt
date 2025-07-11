package cmm.apps.esmorga.view.profile

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.LogOutUseCase
import cmm.apps.esmorga.view.profile.model.ProfileEffect
import cmm.apps.esmorga.view.profile.model.ProfileUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class ProfileViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        loadUser()
    }

    fun logout() {
        clearUserData()
    }

    fun loadUser() {
        viewModelScope.launch {
            getSavedUserUseCase.invoke()
                .onSuccess { user ->
                    _uiState.value = ProfileUiState(user = user)
                }
                .onFailure {
                    _uiState.value = ProfileUiState(user = null)
                }
        }
    }

    fun logIn() {
        _effect.tryEmit(ProfileEffect.NavigateToLogIn)
    }

    internal fun clearUserData() {
        viewModelScope.launch {
            logOutUseCase.invoke()
            _uiState.value = ProfileUiState(user = null)
        }
    }
}