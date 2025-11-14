package cmm.apps.esmorga.datasource_local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cmm.apps.esmorga.datasource_local.poll.model.PollLocalModel
import cmm.apps.esmorga.datasource_local.poll.model.PollOptionLocalModel


@Dao
interface PollDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolls(polls: List<PollLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPollOptions(poll: List<PollOptionLocalModel>)

    @Query(
        "SELECT * FROM PollLocalModel " +
                "JOIN PollOptionLocalModel ON PollLocalModel.localId = PollOptionLocalModel.localPollId"
    )
    suspend fun getPolls(): Map<PollLocalModel, List<PollOptionLocalModel>>

    @Query("DELETE FROM PollLocalModel")
    suspend fun deleteAllPolls()

    @Query("DELETE FROM PollOptionLocalModel")
    suspend fun deleteAllPollOptions()

}