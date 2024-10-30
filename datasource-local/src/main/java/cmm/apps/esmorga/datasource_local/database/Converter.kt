package cmm.apps.esmorga.datasource_local.database

import androidx.room.TypeConverter


class ZonedDateTimeConverter {

    companion object {
        const val CSV_SEPARATOR = ";//;"
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(separator = CSV_SEPARATOR)
    }

    @TypeConverter
    fun fromString(csv: String): List<String> {
        return csv.split(CSV_SEPARATOR)
    }
}
