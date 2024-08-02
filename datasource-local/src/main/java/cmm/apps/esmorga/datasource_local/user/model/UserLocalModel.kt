package cmm.apps.esmorga.datasource_local.user.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserLocalModel(
    @PrimaryKey val localEmail: String,
    val localName: String,
    val localLastName: String,
)