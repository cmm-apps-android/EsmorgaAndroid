package cmm.apps.esmorga.view.datetime

import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.text.DateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EventDateTimeFormatterTest {

    private val previousLocale: Locale = Locale.getDefault()
    private val previousTimeZone: TimeZone = TimeZone.getDefault()

    private val sut: EsmorgaDateTimeFormatter = DateFormatterImpl()

    private var epoch: Long = 0L

    @Before
    fun setup() {
        Locale.setDefault(Locale.UK)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        epoch = ZonedDateTime.of(2025, 9, 26, 16, 44, 0, 0, ZoneId.of("UTC")).toInstant().toEpochMilli()
    }

    @After
    fun tearDown() {
        Locale.setDefault(previousLocale)
        TimeZone.setDefault(previousTimeZone)
    }

    // MOB-343
    @Test
    fun `given epoch millis when formatted in UK locale then structure and components are correct`() {
        val result = sut.formatEventDate(epoch)
        assertTrue("Expected two commas in output, was: $result", result.count { it == ',' } == 2)
        val parts = result.split(", ")
        assertEquals("Expected three parts split by comma and space", 3, parts.size)
        assertEquals("Fri", parts[0])
        val expectedTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(Date(epoch))
        assertEquals(expectedTime, parts[2])
    }

    // MOB-343
    @Test
    fun `given epoch millis when formatted in French locale then medium date and short time are localized`() {
        Locale.setDefault(Locale.FRANCE)
        val result = sut.formatEventDate(epoch)
        val remainder = result.substringAfter(", ")
        val expectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(Date(epoch))
        val expectedTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(Date(epoch))
        assertTrue("Remainder should contain expected date: $remainder vs $expectedDate", remainder.startsWith(expectedDate))
        assertTrue("Remainder should end with expected time: $remainder vs $expectedTime", remainder.endsWith(expectedTime))
    }

    // MOB-343
    @Test
    fun `given epoch millis when formatted in German locale then medium date and short time are localized`() {
        Locale.setDefault(Locale.GERMANY)
        val result = sut.formatEventDate(epoch)
        val remainder = result.substringAfter(", ")
        val expectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(Date(epoch))
        val expectedTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(Date(epoch))
        assertTrue(remainder.startsWith(expectedDate))
        assertTrue(remainder.endsWith(expectedTime) || remainder.endsWith("$expectedTime Uhr"))
    }

    // MOB-343
    @Test
    fun `given epoch millis when formatted in Spanish locale then medium date and short time are localized`() {
        Locale.setDefault(Locale.forLanguageTag("es-ES"))
        val result = sut.formatEventDate(epoch)
        val remainder = result.substringAfter(", ")
        val expectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(Date(epoch))
        val expectedTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(Date(epoch))
        assertTrue(remainder.startsWith(expectedDate))
        assertTrue(remainder.endsWith(expectedTime))
    }

    // MOB-346
    @Test
    fun `given epoch millis when formatted in Italian locale then format clarity rules are met`() {
        Locale.setDefault(Locale.ITALY)
        val result = sut.formatEventDate(epoch)
        assertTrue(result.count { it == ',' } == 2)
        val parts = result.split(", ")
        assertEquals(3, parts.size)
        assertTrue(parts[0].isNotBlank())
        val expectedTime = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(Date(epoch))
        assertTrue(parts[2].endsWith(expectedTime))
    }

    // MOB-343
    @Test
    fun `given epoch millis when formatted in Spanish locale then day and separators present`() {
        Locale.setDefault(Locale.forLanguageTag("es-ES"))
        val result = sut.formatEventDate(epoch)
        assertTrue(result.count { it == ',' } == 2)
        val parts = result.split(", ")
        assertEquals(3, parts.size)
        assertTrue(parts[0].lowercase().isNotEmpty())
    }
}
