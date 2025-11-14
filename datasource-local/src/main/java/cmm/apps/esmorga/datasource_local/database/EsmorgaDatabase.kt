package cmm.apps.esmorga.datasource_local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cmm.apps.esmorga.datasource_local.database.dao.EventAttendeeDao
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.database.dao.PollDao
import cmm.apps.esmorga.datasource_local.database.dao.UserDao
import cmm.apps.esmorga.datasource_local.event.model.EventAttendeeLocalModel
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.datasource_local.poll.model.PollLocalModel
import cmm.apps.esmorga.datasource_local.poll.model.PollOptionLocalModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel

@Database(
    version = EsmorgaDatabaseHelper.DATABASE_VERSION,
    entities = [
        EventLocalModel::class,
        UserLocalModel::class,
        EventAttendeeLocalModel::class,
        PollLocalModel::class,
        PollOptionLocalModel::class
    ],
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)

@TypeConverters(EsmorgaTypeConverter::class)
abstract class EsmorgaDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun eventAttendeeDao(): EventAttendeeDao
    abstract fun pollDao(): PollDao
}