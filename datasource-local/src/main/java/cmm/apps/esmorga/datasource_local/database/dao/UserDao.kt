package cmm.apps.esmorga.datasource_local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserLocalModel)

    @Query("SELECT * FROM UserLocalModel")
    suspend fun getUser(): UserLocalModel

    @Query("DELETE FROM UserLocalModel")
    suspend fun deleteUser()
}