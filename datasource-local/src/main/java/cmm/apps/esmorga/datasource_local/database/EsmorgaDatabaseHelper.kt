package cmm.apps.esmorga.datasource_local.database

import android.content.Context
import androidx.room.Room


object EsmorgaDatabaseHelper {

    private const val DATABASE_NAME = "esmorga_database"
    const val DATABASE_VERSION = 1

    fun getDatabase(context: Context) =
        Room.databaseBuilder(context, EsmorgaDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

}