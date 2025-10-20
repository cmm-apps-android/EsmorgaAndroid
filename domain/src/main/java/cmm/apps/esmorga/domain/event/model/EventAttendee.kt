package cmm.apps.esmorga.domain.event.model

import kotlinx.serialization.Serializable


@Serializable
data class EventAttendee(
    val name: String
)
