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
    fun attendeeListView_lightTheme_normal_user_data() {
        val goodAttendee = AttendeeUiModel(
            name = "Pacitos",
            checked = true
        )
        val badAttendee = AttendeeUiModel(
            name = "Juantxo",
            checked = false
        )

        snapshotWithState(showChecked = false, attendeeList = listOf(goodAttendee, badAttendee))
    }

    @Test
    fun attendeeListView_lightTheme_admin_user_data() {
        val goodAttendee = AttendeeUiModel(
            name = "Neo",
            checked = true
        )
        val badAttendee = AttendeeUiModel(
            name = "Trinity",
            checked = false
        )

        snapshotWithState(showChecked = true, attendeeList = listOf(goodAttendee, badAttendee))
    }

    private fun snapshotWithState(loading: Boolean = false, showChecked: Boolean = false, attendeeList: List<AttendeeUiModel>) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventAttendeesView(
                    uiState = EventAttendeesUiState(loading = loading, showChecked = showChecked, attendeeList = attendeeList),
                    onAttendeeChecked = { pos, checked -> },
                    onBackPressed = { }
                )
            }
        }
    }

}