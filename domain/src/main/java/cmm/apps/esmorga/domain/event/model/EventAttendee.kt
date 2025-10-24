package cmm.apps.esmorga.domain.event.model

import kotlinx.serialization.Serializable


@Serializable
data class EventAttendee(
    val eventId: String,
    val name: String,
    val alreadyPaid: Boolean
)
