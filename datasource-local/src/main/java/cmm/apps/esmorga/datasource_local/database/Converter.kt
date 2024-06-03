package cmm.apps.esmorga.datasource_local.database

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


class ZonedDateTimeConverter {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSVV"
        const val CSV_SEPARATOR = ";//;"
    }

    @TypeConverter
    fun fromTimestamp(dateString: String): ZonedDateTime {
        return ZonedDateTime.from(DateTimeFormatter.ofPattern(DATE_FORMAT).parse(dateString))
    }

    @TypeConverter
    fun toTimestamp(date: ZonedDateTime): String {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(TimeZone.getDefault().toZoneId()))
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
