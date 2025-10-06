package cmm.apps.esmorga.view.screenshot.createEvent

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import cmm.apps.esmorga.view.createeventdate.CreateEventFormDateView
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
class CreateEventFormDateScreenScreenshotTest : BaseScreenshotTest() {
    private val fixedZone: ZoneId = ZoneId.of("UTC")
    private val fixedDate: LocalDate = LocalDate.of(2025, 8, 20)
    private val fixedMillis: Long = fixedDate
        .atStartOfDay(fixedZone)
        .toInstant()
        .toEpochMilli()

    private val fixedSelectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= fixedMillis
        }
    }

    private val fixedDatePickerState = DatePickerState(
        initialSelectedDateMillis = fixedMillis,
        selectableDates = fixedSelectableDates,
        locale = Locale("es", "ES")
    )

    @Before
    fun setUpTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(fixedZone))
    }

    @Test
    fun createEventDateScreenDefault() {
        snapshotWithState(
            isButtonEnabled = false
        )
    }

    @Test
    fun createEventDateScreenWithButtonEnabled() {
        snapshotWithState(
            isButtonEnabled = true
        )
    }

    private fun snapshotWithState(
        isButtonEnabled: Boolean = false
    ) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                CreateEventFormDateView(
                    isButtonEnabled = isButtonEnabled,
                    datePickerState = fixedDatePickerState,
                    onBackPressed = {},
                    onNextClick = { _, _ -> },
                    onTimeSelected = { _ -> },
                    formattedTime = { _, _ -> "12:00:00.000Z" }
                )
            }
        }
    }
}