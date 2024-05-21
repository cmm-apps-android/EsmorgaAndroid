package cmm.apps.esmorga.datasource_local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel


@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: List<EventLocalModel>)

    @Query("SELECT * FROM EventLocalModel")
    suspend fun getEvents(): List<EventLocalModel>

    @Query("DELETE FROM EventLocalModel")
    suspend fun deleteAll()
}