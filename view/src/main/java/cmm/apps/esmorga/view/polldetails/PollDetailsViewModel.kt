package cmm.apps.esmorga.view.polldetails

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cmm.apps.esmorga.domain.poll.VotePollUseCase
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.view.polldetails.mapper.PollDetailsUiMapper.toPollUiDetails
import cmm.apps.esmorga.view.polldetails.model.PollDetailsEffect
import cmm.apps.esmorga.view.polldetails.model.PollDetailsUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PollDetailsViewModel(
    private val votePollUseCase: VotePollUseCase,
    poll: Poll
) : ViewModel(), DefaultLifecycleObserver {

    //Copy of the poll passed as parameter so it can be internally modified by user actions such as vote. Poll in local and remote will be modified in other layers.
    private var internalPoll = poll

    private val _uiState = MutableStateFlow(PollDetailsUiState())
    val uiState: StateFlow<PollDetailsUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<PollDetailsEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<PollDetailsEffect> = _effect.asSharedFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updateUiState()
    }

    fun onBackPressed() {
        _effect.tryEmit(PollDetailsEffect.NavigateBack)
    }

    fun onPrimaryButtonClicked() {
        votePoll()
    }

    private fun votePoll() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPrimaryButtonLoading = true)
            //TODO internalPoll = change poll to reflect the selected options
            votePollUseCase().onSuccess {
                updateUiState()
                _effect.tryEmit(PollDetailsEffect.ShowVoteSuccess)
            }.onFailure { error ->
                showErrorScreen(error)
            }
        }
    }

    private fun updateUiState() {
        _uiState.value = internalPoll.toPollUiDetails()
    }

    private fun showErrorScreen(error: EsmorgaException) {
        _uiState.value = _uiState.value.copy(isPrimaryButtonLoading = false)
        if (error.code == ErrorCodes.NO_CONNECTION) {
            _effect.tryEmit(PollDetailsEffect.ShowNoNetworkError())
        } else {
            _effect.tryEmit(PollDetailsEffect.ShowFullScreenError())
        }
    }
}