package cmm.apps.esmorga.view.dateformatting

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

interface EsmorgaDateTimeFormatter {
    fun formatDateforView(epochMillis: Long): String
    fun formatNotificationDate(epochMillis: Long): String
    fun formatTimeWithMillisUtcSuffix(hour: Int, minute: Int): String
    fun formatIsoDateTime(date: Date, time: String): String
    fun toLocalDateEpochMillis(isoDateTime: String): Long
    fun extractLocalTime(isoDateTime: String): String
}

class DateFormatterImpl : EsmorgaDateTimeFormatter {
    private val TIME_FORMAT_WITH_MILLIS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    private val ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private val TIME_FORMAT_24H = DateTimeFormatter.ofPattern("HH:mm")

    override fun formatTimeWithMillisUtcSuffix(hour: Int, minute: Int): String {
        val localTime = LocalTime.of(hour, minute)
        return localTime.format(TIME_FORMAT_WITH_MILLIS)
    }

    override fun formatIsoDateTime(date: Date, time: String): String {
        val zoneId = ZoneId.systemDefault()
        val localDate = date.toInstant()
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
        val localDateTime = localDate.atTime(LocalTime.parse(time, TIME_FORMAT_WITH_MILLIS))

        return localDateTime
            .atZone(zoneId)
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(ISO_DATE_TIME_FORMATTER)
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

    override fun formatNotificationDate(epochMillis: Long): String {
        try {
            val locale: Locale = Locale.getDefault()
            val zoneId: ZoneId = ZoneId.systemDefault()
            val zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), zoneId)

            val monthName = zonedDateTime.month.getDisplayName(TextStyle.SHORT, locale).lowercase().trimEnd('.')
            val shortTime = zonedDateTime.format(TIME_FORMAT_24H)

            return "${zonedDateTime.dayOfMonth} $monthName, $shortTime"
        } catch (_: Exception) {
            return Instant.ofEpochMilli(epochMillis).toString()
        }
    }

    override fun toLocalDateEpochMillis(isoDateTime: String): Long {
        val zoneId = ZoneId.systemDefault()
        return Instant.parse(isoDateTime)
            .atZone(zoneId)
            .toLocalDate()
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }

    override fun extractLocalTime(isoDateTime: String): String {
        return Instant.parse(isoDateTime)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(TIME_FORMAT_WITH_MILLIS)
    }
}
