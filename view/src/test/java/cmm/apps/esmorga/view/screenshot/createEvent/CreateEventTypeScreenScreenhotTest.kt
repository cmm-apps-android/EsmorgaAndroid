package cmm.apps.esmorga.view.screenshot.createEvent

import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiModel
import cmm.apps.esmorga.view.createeventtype.CreateEventTypeScreen
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class CreateEventTypeScreenScreenshotTest : BaseScreenshotTest() {

    @Test
    fun createEventTypeScreenDefault() {
        val form = CreateEventFormUiModel(
            name = "My Event",
            description = "Some description",
            type = EventType.PARTY
        )

        snapshotWithState(
            form = form,
            selectedEventType = EventType.PARTY
        )
    }

    @Test
    fun createEventTypeScreenWithSportSelected() {
        val form = CreateEventFormUiModel(
            name = "My Event",
            description = "Some description",
            type = null
        )

        snapshotWithState(
            form = form,
            selectedEventType = EventType.SPORT
        )
    }

    @Test
    fun createEventTypeScreenWithCharitySelected() {
        val form = CreateEventFormUiModel(
            name = "Charity Event",
            description = "Helping hands",
            type = null
        )

        snapshotWithState(
            form = form,
            selectedEventType = EventType.CHARITY
        )
    }

    @Test
    fun createEventTypeScreenWithFoodSelected() {
        val form = CreateEventFormUiModel(
            name = "Food Event",
            description = "Dinner with friends",
            type = null
        )

        snapshotWithState(
            form = form,
            selectedEventType = EventType.FOOD
        )
    }

    @Test
    fun createEventTypeScreenWithGamesSelected() {
        val form = CreateEventFormUiModel(
            name = "Games Night",
            description = "Board games and fun",
            type = null
        )

        snapshotWithState(
            form = form,
            selectedEventType = EventType.GAMES
        )
    }

    private fun snapshotWithState(
        form: CreateEventFormUiModel,
        selectedEventType: EventType
    ) {
        val updatedForm = form.copy(type = selectedEventType)

        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                CreateEventTypeScreen(
                    eventForm = updatedForm,
                    onBackClick = {},
                    onNextClick = {}
                )
            }
        }
    }
}