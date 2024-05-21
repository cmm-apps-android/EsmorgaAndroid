package cmm.apps.esmorga.datasource_local.database

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


class ZonedDateTimeConverter {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSVV"
    }

    @TypeConverter
    fun fromTimestamp(dateString: String): ZonedDateTime {
        return ZonedDateTime.from(DateTimeFormatter.ofPattern(DATE_FORMAT).parse(dateString))
    }

    @TypeConverter
    fun toTimestamp(date: ZonedDateTime): String {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(TimeZone.getDefault().toZoneId()))
    }

}
