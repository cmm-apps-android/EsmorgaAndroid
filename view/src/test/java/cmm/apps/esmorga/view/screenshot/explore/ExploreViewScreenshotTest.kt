package cmm.apps.esmorga.view.screenshot.explore

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.explore.EventListView
import cmm.apps.esmorga.view.explore.model.ListCardUiModel
import cmm.apps.esmorga.view.explore.model.ExploreUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class ExploreViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun eventListView_lightTheme_empty() {
        snapshotWithState(loading = false, eventList = listOf(), pollList = listOf(), error = null)
    }

    @Test
    fun eventListView_lightTheme_error() {
        snapshotWithState(loading = false, eventList = listOf(), pollList = listOf(), error = "Error")
    }

    @Test
    fun eventListView_lightTheme_loading() {
        snapshotWithState(loading = true, eventList = listOf(), pollList = listOf(), error = null)
    }

    @Test
    fun eventListView_lightTheme_events() {
        val event = ListCardUiModel(
            id = "1",
            imageUrl = "test.png",
            cardTitle = "Event Title",
            cardSubtitle1 = "Event subtitle 1",
            cardSubtitle2 = "Event subtitle 2",
        )

        snapshotWithState(loading = false, eventList = listOf(event, event.copy(id = "2")), pollList = listOf(), error = null)
    }

    @Test
    fun eventListView_lightTheme_polls() {
        val poll = ListCardUiModel(
            id = "1",
            imageUrl = "test.png",
            cardTitle = "Poll Title",
            cardSubtitle1 = "Poll subtitle 1"
        )

        snapshotWithState(loading = false, eventList = listOf(), pollList = listOf(poll), error = null)
    }

    @Test
    fun eventListView_lightTheme_events_and_polls() {
        val event = ListCardUiModel(
            id = "1",
            imageUrl = "test.png",
            cardTitle = "Event Title",
            cardSubtitle1 = "Event subtitle 1",
            cardSubtitle2 = "Event subtitle 2",
        )

        val poll = ListCardUiModel(
            id = "1",
            imageUrl = "test.png",
            cardTitle = "Poll Title",
            cardSubtitle1 = "Poll subtitle 1"
        )

        snapshotWithState(loading = false, eventList = listOf(event), pollList = listOf(poll), error = null)
    }

    private fun snapshotWithState(loading: Boolean, eventList: List<ListCardUiModel>, pollList: List<ListCardUiModel>, error: String?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventListView(
                    uiState = ExploreUiState(loading = loading, eventList = eventList, pollList = pollList, error = error),
                    snackbarHostState = SnackbarHostState(),
                    onRetryClick = { },
                    onEventClick = { },
                    onPollClick = { }
                )
            }
        }
    }

}