package cmm.apps.esmorga.datasource_local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.database.dao.UserDao
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel

@Database(
    version = EsmorgaDatabaseHelper.DATABASE_VERSION,
    entities = [
        EventLocalModel::class,
        UserLocalModel::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)

@TypeConverters(ZonedDateTimeConverter::class)
abstract class EsmorgaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
}