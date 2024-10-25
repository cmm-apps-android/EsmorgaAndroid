package cmm.apps.esmorga.view.screenshot.eventList

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.eventlist.MyEventListView
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.MyEventListError
import cmm.apps.esmorga.view.eventlist.model.MyEventListUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class MyEventListViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun myEventListView_lightTheme_error_not_logged_in() {
        snapshotWithState(loading = false, eventList = listOf(), error = MyEventListError.NOT_LOGGED_IN)
    }

    @Test
    fun myEventListView_lightTheme_error_no_events_joined() {
        snapshotWithState(loading = false, eventList = listOf(), error = MyEventListError.EMPTY_LIST)
    }

    @Test
    fun myEventListView_lightTheme_loading() {
        snapshotWithState(loading = true, eventList = listOf())
    }

    @Test
    fun eventListView_lightTheme_data() {
        val event = EventListUiModel(
            id = "1",
            imageUrl = "test.png",
            cardTitle = "Card Title",
            cardSubtitle1 = "Card subtitle 1",
            cardSubtitle2 = "Card subtitle 2",
        )

        snapshotWithState(loading = false, eventList = listOf(event, event))
    }

    private fun snapshotWithState(loading: Boolean, eventList: List<EventListUiModel>, error: MyEventListError? = null) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                MyEventListView(uiState = MyEventListUiState(loading = loading, eventList = eventList, error = error),
                    snackbarHostState = SnackbarHostState(),
                    onEventClick = { },
                    onSignInClick = { },
                    onRetryClick = { }
                )
            }
        }
    }
}