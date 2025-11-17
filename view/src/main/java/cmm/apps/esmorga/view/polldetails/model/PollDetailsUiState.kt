package cmm.apps.esmorga.view.polldetails.model

import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaNoNetworkScreenArguments

data class PollDetailsUiState(
    val id: String = "",
    val title: String = "",
    val voteDeadline: String = "",
    val description: String = "",
    val image: String? = null,
    val isPrimaryButtonLoading: Boolean = false,
    val isPrimaryButtonEnabled: Boolean = true,
    val primaryButtonTitle: String = "",
    val isMultipleChoice: Boolean = false,
    val options: List<String> = emptyList()
)

sealed class PollDetailsEffect {
    data object NavigateBack : PollDetailsEffect()
    data object ShowVoteSuccess : PollDetailsEffect()
    data class ShowNoNetworkError(val esmorgaNoNetworkArguments: EsmorgaErrorScreenArguments = getEsmorgaNoNetworkScreenArguments()) : PollDetailsEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : PollDetailsEffect()
}
