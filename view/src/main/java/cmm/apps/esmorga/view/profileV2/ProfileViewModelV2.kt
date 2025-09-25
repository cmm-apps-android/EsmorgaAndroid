package cmm.apps.esmorga.view.profileV2

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.user.GetSavedUserUseCaseV2
import cmm.apps.esmorga.view.profileV2.model.ProfileUiStateV2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileViewModelV2(
    private val getSavedUserCaseV2: GetSavedUserUseCaseV2
) : ViewModel(), DefaultLifecycleObserver {

    val uiState = MutableStateFlow(ProfileUiStateV2())

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        showUser()
    }

    fun showUser() {
        viewModelScope.launch {
            getSavedUserCaseV2.invoke()
                .onSuccess { user ->
                    uiState.value = ProfileUiStateV2(user = user)
                }
                .onFailure {
                    uiState.value = ProfileUiStateV2(user = null)
                }
        }
    }
}