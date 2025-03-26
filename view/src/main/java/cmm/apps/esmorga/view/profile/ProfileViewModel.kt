package cmm.apps.esmorga.view.profile

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.LogOutUseCase
import cmm.apps.esmorga.view.profile.model.ProfileEffect
import cmm.apps.esmorga.view.profile.model.ProfileUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val context: Context
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

    fun changePassword() {
        viewModelScope.launch {
            val effect = if (isInternetAvailable()) {
                ProfileEffect.NavigateToChangePassword
            } else {
                ProfileEffect.ShowNoNetworkError()
            }
            _effect.emit(effect)
        }
    }

    private fun loadUser() {
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
            _effect.emit(ProfileEffect.NavigateToLogIn)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}
