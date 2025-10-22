package cmm.apps.esmorga.datasource_local.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object EsmorgaDatabaseHelper {

    private const val DATABASE_NAME = "esmorga_database"
    const val DATABASE_VERSION = 5

    fun getDatabase(context: Context) =
        Room.databaseBuilder(context, EsmorgaDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
            .fallbackToDestructiveMigration()
            .build()

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE UserLocalModel ADD COLUMN localRole TEXT NOT NULL DEFAULT 'USER'")
        }
    }

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE IF EXISTS EventLocalModel")
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS EventLocalModel (
                localId TEXT NOT NULL PRIMARY KEY,
                localName TEXT NOT NULL,
                localDate INTEGER NOT NULL,
                localDescription TEXT NOT NULL,
                localType TEXT NOT NULL,
                localImageUrl TEXT,
                localLocationName TEXT NOT NULL,
                localLocationLat REAL,
                localLocationLong REAL,
                localTags TEXT NOT NULL,
                localCreationTime INTEGER NOT NULL,
                localUserJoined INTEGER NOT NULL DEFAULT 0,
                localCurrentAttendeeCount INTEGER NOT NULL DEFAULT 0,
                localMaxCapacity INTEGER,
                localJoinDeadline INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
            )
        }
    }
}