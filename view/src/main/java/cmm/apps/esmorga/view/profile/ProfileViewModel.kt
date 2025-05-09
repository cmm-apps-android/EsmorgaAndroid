package cmm.apps.esmorga.view.profile

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.common.util.ConnectivityUtils
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.LogOutUseCase
import cmm.apps.esmorga.view.profile.model.ProfileEffect
import cmm.apps.esmorga.view.profile.model.ProfileUiState
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
    private val logOutUseCase: LogOutUseCase,
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

    fun changePassword(context: Context) {
        viewModelScope.launch {
            ConnectivityUtils.reportNoConnectivityIfNeeded(context)

            val effect = if (ConnectivityUtils.isNetworkAvailable(context)) {
                ProfileEffect.NavigateToChangePassword
            } else {
                ProfileEffect.ShowNoNetworkError()
            }
            _effect.emit(effect)
        }
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
            _effect.emit(ProfileEffect.NavigateToLogIn)
        }
    }

    open fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}