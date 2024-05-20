package cmm.apps.esmorga.domain.event.model

import java.time.ZonedDateTime


data class Event(
    val name: String,
    val date: ZonedDateTime
)
