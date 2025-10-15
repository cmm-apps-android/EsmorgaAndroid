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
            .build()

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE UserLocalModel ADD COLUMN localRole TEXT NOT NULL DEFAULT 'USER'")
        }
    }

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE IF EXISTS EventLocalModel")
        }
    }
}