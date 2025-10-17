package cmm.apps.esmorga.datasource_remote.datetime

import cmm.apps.esmorga.datasource_remote.dateformatting.EsmorgaRemoteDateFormatter
import cmm.apps.esmorga.datasource_remote.dateformatting.RemoteDateFormatterImpl
import org.junit.Before
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneOffset

class RemoteDateFormatterTest {

    private lateinit var sut: EsmorgaRemoteDateFormatter

    @Before
    fun setup() {
        sut = RemoteDateFormatterImpl()
    }

    @Test
    fun `given valid ISO date string with milliseconds and zone when parsed then returns correct ZonedDateTime`() {
        val dateString = "2025-09-26T16:44:00.123+00:00"
        val result = sut.parseIsoDateTime(dateString)

        assertEquals(2025, result.year)
        assertEquals(9, result.monthValue)
        assertEquals(26, result.dayOfMonth)
        assertEquals(16, result.hour)
        assertEquals(44, result.minute)
        assertEquals(0, result.second)
        assertEquals(123_000_000, result.nano)
        assertEquals(ZoneOffset.UTC, result.offset)
    }

    @Test
    fun `given ISO date string with different timezone when parsed then returns correct ZonedDateTime`() {
        val dateString = "2025-09-26T18:44:00.000+02:00"
        val result = sut.parseIsoDateTime(dateString)

        assertEquals(2025, result.year)
        assertEquals(9, result.monthValue)
        assertEquals(26, result.dayOfMonth)
        assertEquals(18, result.hour)
        assertEquals(44, result.minute)
        assertEquals(0, result.second)
        assertEquals(0, result.nano)
        assertEquals(ZoneOffset.ofHours(2), result.offset)
    }

    @Test
    fun `given malformed date string when parsed then throws EsmorgaException`() {
        val invalidDate = "invalid-date-string"

        try {
            sut.parseIsoDateTime(invalidDate)
            assertTrue("Expected EsmorgaException to be thrown", false)
        } catch (e: EsmorgaException) {
            assertEquals(ErrorCodes.PARSE_ERROR, e.code)
            assertEquals("Error parsing remote date: $invalidDate", e.message)
        }
    }

    @Test
    fun `given empty date string when parsed then throws EsmorgaException`() {
        val emptyDate = ""

        try {
            sut.parseIsoDateTime(emptyDate)
            assertTrue("Expected EsmorgaException to be thrown", false)
        } catch (e: EsmorgaException) {
            assertEquals(ErrorCodes.PARSE_ERROR, e.code)
            assertEquals("Error parsing remote date: $emptyDate", e.message)
        }
    }
}