package cmm.apps.esmorga.datasource_local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.database.dao.UserDao
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel

@Database(
    entities = [
        EventLocalModel::class,
        UserLocalModel::class
    ], version = EsmorgaDatabaseHelper.DATABASE_VERSION, exportSchema = true
)

@TypeConverters(ZonedDateTimeConverter::class)
abstract class EsmorgaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
}