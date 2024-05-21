package cmm.apps.esmorga.datasource_local.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel


interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: EventLocalModel): Long

    @Query("SELECT * FROM EventLocalModel")
    fun getEvents(): List<EventLocalModel>
}