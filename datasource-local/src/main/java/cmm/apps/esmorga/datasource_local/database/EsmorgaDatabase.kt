package cmm.apps.esmorga.datasource_local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel

@Database(
    entities = [
        EventLocalModel::class
    ], version = EsmorgaDatabaseHelper.DATABASE_VERSION, exportSchema = true
)

@TypeConverters(ZonedDateTimeConverter::class)
abstract class EsmorgaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}