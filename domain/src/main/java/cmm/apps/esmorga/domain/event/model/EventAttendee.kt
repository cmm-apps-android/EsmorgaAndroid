package cmm.apps.esmorga.domain.event.model


data class EventAttendee(
    val eventId: String,
    val name: String,
    val alreadyPaid: Boolean
)
