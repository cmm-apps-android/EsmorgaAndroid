package cmm.apps.esmorga.datasource_local.event.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity
data class EventLocalModel(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val localName: String,
    val localDate: ZonedDateTime,
    val creationTime: Long
)