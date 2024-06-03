package cmm.apps.esmorga.datasource_local.event.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime

@Entity
data class EventLocalModel(
    @PrimaryKey val localId: String,
    val localName: String,
    val localDate: ZonedDateTime,
    val localDescription: String,
    val localType: String,
    val localImageUrl: String? = null,
    val localLocationName: String,
    val localLocationLat: Double? = null,
    val localLocationLong: Double? = null,
    val localTags: List<String> = listOf(),
    val localCreationTime: Long
)