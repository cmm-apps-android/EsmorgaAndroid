package cmm.apps.esmorga.view.profileV2

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.GetSavedUserUseCaseV2
import cmm.apps.esmorga.domain.user.LogOutUseCaseV2
import cmm.apps.esmorga.view.profileV2.model.ProfileUiStateV2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModelV2(
    private val getSavedUserCaseV2: GetSavedUserUseCaseV2,
    private val logOutUseCaseV2UseCase: LogOutUseCaseV2
) : ViewModel(), DefaultLifecycleObserver {

    private val _uiState = MutableStateFlow(ProfileUiStateV2())
    val uiState: StateFlow<ProfileUiStateV2> = _uiState.asStateFlow()

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
}