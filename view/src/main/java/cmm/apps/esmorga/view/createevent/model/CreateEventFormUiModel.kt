package cmm.apps.esmorga.view.createevent.model

import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import kotlinx.serialization.Serializable

@Serializable
data class CreateEventFormUiModel(
    val name: String? = null,
    val description: String? = null,
    val type: EventType? = null,
    val imageUrl: String? = null,
    val location: EventLocation? = null,
    val tags: List<String> = emptyList()
)