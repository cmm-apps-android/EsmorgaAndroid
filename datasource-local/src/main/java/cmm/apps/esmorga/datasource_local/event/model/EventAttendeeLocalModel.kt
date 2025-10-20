package cmm.apps.esmorga.datasource_local.event.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventAttendeeLocalModel(
    @PrimaryKey val localName: String,
    val localEventId: String,
    val localCreationTime: Long
)
