package cmm.apps.esmorga.datasource_local.event.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["localEventId", "localName"])
data class EventAttendeeLocalModel(
    val localEventId: String,
    val localName: String,
    @ColumnInfo(defaultValue = "0") val localAlreadyPaid: Boolean
)