package cmm.apps.esmorga.view.createevent.createeventimage.model

import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments

data class CreateEventFormImageUiState(
    val imageUrl: String = "",
    val showPreview: Boolean = false,
    val isLoading: Boolean = false,
    val imageError: Int? = null,
)

sealed class CreateEventFormImageEffect {
    data object NavigateBack : CreateEventFormImageEffect()
    data class ShowCreationSuccess(val message: String) : CreateEventFormImageEffect()
    data class ShowCreationError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments) : CreateEventFormImageEffect()
    data class ShowNoInternetError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments) : CreateEventFormImageEffect()
}
