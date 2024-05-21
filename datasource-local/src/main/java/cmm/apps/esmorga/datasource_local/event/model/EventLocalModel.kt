package cmm.apps.esmorga.datasource_local.event.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity
data class EventLocalModel(
    @PrimaryKey val localId: Long = 0,
    val localName: String,
    val localDate: ZonedDateTime
)