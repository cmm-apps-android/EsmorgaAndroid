package cmm.apps.esmorga.datasource_local.poll.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PollLocalModel(
    @PrimaryKey val localId: String,
    val localName: String,
    val localDescription: String,
    val localImageUrl: String? = null,
    val localVoteDeadline: Long,
    @ColumnInfo(defaultValue = "0") val localIsMultipleChoice: Boolean, //SQLite does not support Boolean, INTEGER is used
    val localCreationTime: Long
)

@Entity(primaryKeys = ["localPollId", "localOptionId"])
data class PollOptionLocalModel(
    val localPollId: String,
    val localOptionId: String,
    val localText: String,
    val localVoteCount: Int,
    @ColumnInfo(defaultValue = "0") val localUserSelected: Boolean, //SQLite does not support Boolean, INTEGER is used
)