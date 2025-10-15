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
            .addMigrations(MIGRATION_4_5)
            .fallbackToDestructiveMigration()
            .build()

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE UserLocalModel ADD COLUMN localRole TEXT NOT NULL DEFAULT 'USER'")
        }
    }

}