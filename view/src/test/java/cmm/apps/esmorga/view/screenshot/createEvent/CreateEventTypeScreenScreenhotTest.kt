package cmm.apps.esmorga.view.screenshot.createEvent

import cmm.apps.esmorga.view.createeventtype.CreateEventTypeScreen
import cmm.apps.esmorga.view.createeventtype.CreateEventTypeViewModel
import cmm.apps.esmorga.view.createeventtype.EventType
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class CreateEventTypeScreenScreenshotTest : BaseScreenshotTest() {

    @Test
    fun createEventTypeScreenDefault() {
        snapshotWithState(
            eventName = "My Event",
            description = "Some description",
            selectedEventType = EventType.Party
        )
    }

    @Test
    fun createEventTypeScreenWithSportSelected() {
        snapshotWithState(
            eventName = "My Event",
            description = "Some description",
            selectedEventType = EventType.Sport
        )
    }

    @Test
    fun createEventTypeScreenWithCharitySelected() {
        snapshotWithState(
            eventName = "Charity Event",
            description = "Helping hands",
            selectedEventType = EventType.Charity
        )
    }

    @Test
    fun createEventTypeScreenWithFoodSelected() {
        snapshotWithState(
            eventName = "Food Event",
            description = "Dinner with friends",
            selectedEventType = EventType.Food
        )
    }

    @Test
    fun createEventTypeScreenWithGamesSelected() {
        snapshotWithState(
            eventName = "Games Night",
            description = "Board games and fun",
            selectedEventType = EventType.Games
        )
    }
    private fun snapshotWithState(
        eventName: String,
        description: String,
        selectedEventType: EventType
    ) {
        val viewModel = CreateEventTypeViewModel(
            eventName = eventName,
            description = description
        ).apply {
            onEventTypeSelected(selectedEventType)
        }

        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                CreateEventTypeScreen(
                    viewModel = viewModel,
                    onBackClick = {},
                    onNextClick = {}
                )
            }
        }
    }
}