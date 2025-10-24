package cmm.apps.esmorga.view.screenshot.eventAttendees

import cmm.apps.esmorga.view.eventattendees.EventAttendeesView
import cmm.apps.esmorga.view.eventattendees.model.AttendeeUiModel
import cmm.apps.esmorga.view.eventattendees.model.EventAttendeesUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class EventAttendeesViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun attendeeListView_lightTheme_loading() {
        snapshotWithState(loading = true, attendeeList = listOf())
    }

    @Test
    fun attendeeListView_lightTheme_data() {
        val goodAttendee = AttendeeUiModel(
            name = "1. Pacitos",
            checked = true
        )
        val badAttendee = AttendeeUiModel(
            name = "2. Villambrosa",
            checked = false
        )

        snapshotWithState(loading = false, attendeeList = listOf(goodAttendee, badAttendee))
    }

    private fun snapshotWithState(loading: Boolean, attendeeList: List<AttendeeUiModel>) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventAttendeesView(
                    uiState = EventAttendeesUiState(loading = loading, attendeeList = attendeeList),
                    onAttendeeChecked = { pos, checked -> },
                    onBackPressed = { }
                )
            }
        }
    }

}