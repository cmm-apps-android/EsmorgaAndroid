package cmm.apps.esmorga.view.screenshot.createEvent

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import cmm.apps.esmorga.view.createeventdate.CreateEventFormDateView
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
class CreateEventFormDateScreenScreenshotTest : BaseScreenshotTest() {
    private val fixedDate = LocalDate.of(2025, 8, 20)
    private val fixedMillis = fixedDate.atStartOfDay(ZoneId.systemDefault())
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
        locale = Locale.getDefault()
    )

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