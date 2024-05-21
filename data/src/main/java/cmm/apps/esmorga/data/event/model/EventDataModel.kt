package cmm.apps.esmorga.data.event.model

import java.time.ZonedDateTime


data class EventDataModel(
    val dataName: String,
    val dataDate: ZonedDateTime,
    val creationTime: Long = System.currentTimeMillis()
)
