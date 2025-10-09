package cmm.apps.esmorga.view.dateformatting

import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object DateFormatter {
    private val TIME_FORMAT_WITH_MILLIS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS'Z'")
    private val ISO_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE

    fun formatTimeWithMillisUtcSuffix(hour: Int, minute: Int): String {
        val localTime = LocalTime.of(hour, minute)
        return localTime.format(TIME_FORMAT_WITH_MILLIS)
    }

    fun formatIsoDateTime(date: Date, time: String): String {
        val localDateTime = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val datePart = localDateTime.format(ISO_DATE_FORMAT)

        return "${datePart}T${time}"
    }
}