package cmm.apps.esmorga.view.eventattendees.model

import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper.getEsmorgaNoNetworkScreenArguments

data class EventAttendeesUiState(
    val loading: Boolean = false,
    val nameList: List<String> = listOf()
)

sealed class EventAttendeesEffect {
    data object NavigateBack : EventAttendeesEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : EventAttendeesEffect()
    data class ShowNoNetworkError(val esmorgaNoNetworkArguments: EsmorgaErrorScreenArguments = getEsmorgaNoNetworkScreenArguments()) : EventAttendeesEffect()
}
