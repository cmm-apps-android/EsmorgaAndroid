package cmm.apps.esmorga.datasource_local.database.dao

import androidx.room.Dao
import androidx.room.Query
import cmm.apps.esmorga.datasource_local.event.model.EventAttendeeLocalModel


@Dao
interface EventAttendeeDao {

    @Query("SELECT * FROM EventAttendeeLocalModel WHERE localEventId = :eventId")
    suspend fun getEventAttendees(eventId: String): List<EventAttendeeLocalModel>

}