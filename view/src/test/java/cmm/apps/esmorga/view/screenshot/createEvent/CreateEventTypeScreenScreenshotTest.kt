package cmm.apps.esmorga.view.screenshot.createEvent

import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createeventtype.CreateEventFormTypeView
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class CreateEventFormTypeScreenScreenshotTest : BaseScreenshotTest() {

    @Test
    fun createEventTypeScreenDefault() {
        snapshotWithState(
            selectedEventType = EventType.PARTY
        )
    }

    @Test
    fun createEventTypeScreenWithSportSelected() {
        snapshotWithState(
            selectedEventType = EventType.SPORT
        )
    }

    @Test
    fun createEventTypeScreenWithCharitySelected() {
        snapshotWithState(
            selectedEventType = EventType.CHARITY
        )
    }

    @Test
    fun createEventTypeScreenWithFoodSelected() {
        snapshotWithState(
            selectedEventType = EventType.FOOD
        )
    }

    @Test
    fun createEventTypeScreenWithGamesSelected() {
        snapshotWithState(
            selectedEventType = EventType.GAMES
        )
    }

    private fun snapshotWithState(
        selectedEventType: EventType
    ) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                CreateEventFormTypeView(
                    eventType = selectedEventType,
                    onBackClick = {},
                    onNextClick = {},
                    onEventTypeSelected = { _ -> }
                )
            }
        }
    }
}