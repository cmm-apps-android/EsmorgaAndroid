package cmm.apps.esmorga.view.dateformatting

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

interface EsmorgaDateTimeFormatter {
    fun formatDateforView(epochMillis: Long): String
    fun formatTimeWithMillisUtcSuffix(hour: Int, minute: Int): String
    fun formatIsoDateTime(date: Date, time: String): String
}

class DateFormatterImpl : EsmorgaDateTimeFormatter {
    private val TIME_FORMAT_WITH_MILLIS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS'Z'")
    private val ISO_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE

    override fun formatTimeWithMillisUtcSuffix(hour: Int, minute: Int): String {
        val localTime = LocalTime.of(hour, minute)
        return localTime.format(TIME_FORMAT_WITH_MILLIS)
    }

    override fun formatIsoDateTime(date: Date, time: String): String {
        val localDateTime = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val datePart = localDateTime.format(ISO_DATE_FORMAT)

        return "${datePart}T${time}"
    }

    override fun formatDateforView(epochMillis: Long): String {
        try {
            val locale: Locale = Locale.getDefault()
            val zoneId: ZoneId = ZoneId.systemDefault()
            val zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), zoneId)

            val mediumDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)
            val shortTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale)

            val dayOfWeek = zonedDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).replaceFirstChar { it.uppercase() }
            val mediumDate = zonedDateTime.format(mediumDateFormatter)
            val shortTime = zonedDateTime.format(shortTimeFormatter)
            return "$dayOfWeek, $mediumDate, $shortTime"
        } catch (_: Exception) {
            return Instant.ofEpochMilli(epochMillis).toString()
        }
    }
}