package cmm.apps.esmorga.datasource_local.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object EsmorgaDatabaseHelper {

    private const val DATABASE_NAME = "esmorga_database"
    const val DATABASE_VERSION = 4

    fun getDatabase(context: Context) =
        Room.databaseBuilder(context, EsmorgaDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_3_4)
            .fallbackToDestructiveMigration()
            .build()

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE UserLocalModel ADD COLUMN localRole TEXT NOT NULL DEFAULT 'USER'")
        }
    }

}