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
    private val fixedDate: LocalDate = LocalDate.of(2033, 8, 20)
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
        locale = Locale.US
    )

    @Before
    fun setUpTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(fixedZone))
    }

    //When recording, uncomment this to produce an screenshot with weekday names with 3 letters.
    //That screenshot should not be commited and this test should be kept commented-out.
    //For some reason, the combination of Paparazzi, DatePicker, and Locale is not producing a consistent result.
    //I have tested many different options, but I did not managed to get a solution.
    //The first recorded test always produces a DatePicker where weekday names are shown with 3 letters. Then the following tests produce a picker with 1 letter names instead.
//    @Test
//    fun a_sacrificial_warmup_test_that_produces_an_invalid_output() {
//        snapshotWithState(
//            isButtonEnabled = false
//        )
//    }

    @Test
    fun createEventDate_lightTheme_button_disabled() {
        snapshotWithState(
            isButtonEnabled = false
        )
    }

    @Test
    fun createEventDate_lightTheme_button_enabled() {
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
                    formattedTime = { _, _ -> "12:00:00" }
                )
            }
        }
    }
}