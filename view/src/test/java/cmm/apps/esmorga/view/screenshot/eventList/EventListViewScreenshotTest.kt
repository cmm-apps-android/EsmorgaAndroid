package cmm.apps.esmorga.view.screenshot.eventList

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.eventlist.EventListView
import cmm.apps.esmorga.view.eventlist.model.EventListUiState
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.EventUILocation
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class EventListViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun eventListView_lightTheme_empty() {
        snapshotWithState(loading = false, eventList = listOf(), error = null)
    }

    @Test
    fun eventListView_lightTheme_error() {
        snapshotWithState(loading = false, eventList = listOf(), error = "Error")
    }

    @Test
    fun eventListView_lightTheme_loading() {
        snapshotWithState(loading = true, eventList = listOf(), error = null)
    }

    @Test
    fun eventListView_lightTheme_data() {
        val event = EventListUiModel(
            id = "1",
            imageUrl = "test.png",
            name = "Card Title",
            dateFormatted = "Card subtitle 1",
            location = EventUILocation("Card subtitle 2"),
            date = "",
            description = "",
            type = EventType.FOOD,
            userJoined = false
        )

        snapshotWithState(loading = false, eventList = listOf(event, event), error = null)
    }

    private fun snapshotWithState(loading: Boolean, eventList: List<EventListUiModel>, error: String?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventListView(
                    uiState = EventListUiState(loading = loading, eventList = eventList, error = error),
                    snackbarHostState = SnackbarHostState(),
                    onRetryClick = { },
                    onEventClick = { }
                )
            }
        }
    }

}