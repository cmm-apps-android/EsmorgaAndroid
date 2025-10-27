package cmm.apps.esmorga.datasource_local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cmm.apps.esmorga.datasource_local.event.model.EventAttendeeLocalModel

@Dao
interface EventAttendeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendee(attendee: EventAttendeeLocalModel)

    @Query("SELECT * FROM EventAttendeeLocalModel WHERE localEventId = :eventId")
    suspend fun getEventAttendees(eventId: String): List<EventAttendeeLocalModel>

}