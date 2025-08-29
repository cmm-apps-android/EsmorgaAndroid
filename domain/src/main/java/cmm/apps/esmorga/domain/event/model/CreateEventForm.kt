package cmm.apps.esmorga.domain.event.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateEventForm(
    val name: String? = null,
    val description: String? = null,
    val type: EventType? = null,
    val imageUrl: String? = null,
    val location: EventLocation? = null,
    val tags: List<String> = emptyList()
)