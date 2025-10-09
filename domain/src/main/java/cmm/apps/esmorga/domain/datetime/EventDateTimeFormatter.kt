package cmm.apps.esmorga.domain.datetime

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

interface EventDateTimeFormatter {
    fun formatEventDate(epochMillis: Long): String
}

class EventDateTimeFormatterImpl : EventDateTimeFormatter {
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
