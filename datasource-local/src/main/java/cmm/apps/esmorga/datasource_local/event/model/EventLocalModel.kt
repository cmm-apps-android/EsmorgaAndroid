package cmm.apps.esmorga.datasource_local.event.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val localCreationTime: Long,
    @ColumnInfo(defaultValue = "0") val localUserJoined: Boolean //SQLite does not support Boolean, INTEGER is used
)