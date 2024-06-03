package cmm.apps.esmorga.data.event.model

import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


data class EventDataModel(
    val dataId: String,
    val dataName: String,
    val dataDate: ZonedDateTime,
    val dataDescription: String,
    val dataType: EventType,
    val dataImageUrl: String? = null,
    val dataLocation: EventLocationDataModel,
    val dataTags: List<String> = listOf(),
    val dataCreationTime: Long = System.currentTimeMillis()
)

data class EventLocationDataModel(
    val name: String,
    val lat: Double? = null,
    val long: Double? = null
)
