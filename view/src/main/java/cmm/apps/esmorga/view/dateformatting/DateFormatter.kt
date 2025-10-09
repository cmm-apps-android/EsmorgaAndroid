package cmm.apps.esmorga.view.dateformatting

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

interface EsmorgaDateTimeFormatter {
    fun formatEventDate(epochMillis: Long): String
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

    override fun formatEventDate(epochMillis: Long): String = try {
        val locale: Locale = Locale.getDefault()
        val tz: TimeZone = TimeZone.getDefault()
        val date = Date(epochMillis)

        val dowFormatter = SimpleDateFormat("EEE", locale).apply { timeZone = tz }
        val mediumDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).apply { timeZone = tz }
        val shortTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, locale).apply { timeZone = tz }

        val dayOfWeek = dowFormatter.format(date).replaceFirstChar { it.uppercase() }
        val mediumDate = mediumDateFormatter.format(date)
        val shortTime = shortTimeFormatter.format(date)
        "$dayOfWeek, $mediumDate, $shortTime"
    } catch (_: Exception) {
        Date(epochMillis).toInstant().toString()
    }
}