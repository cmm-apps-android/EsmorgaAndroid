package cmm.apps.esmorga.view.createevent.createeventimage

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventimage.model.CreateEventFormImageEffect
import cmm.apps.esmorga.view.createevent.createeventimage.model.CreateEventFormImageUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateEventFormImageViewModel(
    private val eventForm: CreateEventForm
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventFormImageUiState())
    val uiState: StateFlow<CreateEventFormImageUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormImageEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventFormImageEffect> = _effect.asSharedFlow()

    private val urlRegex = Regex("""^https?://.+\.(jpe?g|png|gif|webp)(?:\?.*)?$""", RegexOption.IGNORE_CASE)

    fun onBackClick() {
        _effect.tryEmit(CreateEventFormImageEffect.NavigateBack)
    }

    fun onImageUrlChanged(text: String) {
        _uiState.update { state ->
            state.copy(imageUrl = text, imageError = null)
        }
    }

    fun onPreviewClick() {
        val url = _uiState.value.imageUrl.trim()

        if (url.isBlank()) {
            _uiState.update { it.copy(imageError = R.string.inline_error_image_url_required) }
            return
        }

        if (!isValidImageUrl(url)) {
            _uiState.update { it.copy(imageError = R.string.inline_error_image_url_required) }
            return
        }

        _uiState.update { it.copy(showPreview = true, imageError = null) }
    }

    fun onDeleteImageClick() {
        _uiState.update { state ->
            state.copy(imageUrl = "", showPreview = false, imageError = null)
        }
    }

    fun onCreateEventClick() {
        val state = _uiState.value
        val updatedForm = eventForm.copy(
            imageUrl = if (state.showPreview) state.imageUrl else null
        )

        _effect.tryEmit(CreateEventFormImageEffect.NavigateNext(updatedForm))
    }

    private fun isValidImageUrl(url: String): Boolean {
        return url.matches(urlRegex)
    }
}
