package cmm.apps.esmorga.view.screenshot.eventdetails

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.eventdetails.EventDetailsView
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.manipulation.Ordering
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module

class EventDetailsScreenshotTest : BaseScreenshotTest() {
    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    single { mockk<Ordering.Context>(relaxed = true) }
                    single { mockk<EsmorgaDateTimeFormatter>(relaxed = true) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun eventDetailsView_lightTheme_data() {
        snapshotWithState()
    }

    @Test
    fun eventDetailsView_lightTheme_no_location() {
        snapshotWithState(showNavigateButton = false)
    }

    @Test
    fun eventDetailsView_lightTheme_data_user_event_joined() {
        snapshotWithState(buttonTitle = "Leave Event")
    }

    @Test
    fun eventDetailsView_lightTheme_data_user_event_not_joined() {
        snapshotWithState(buttonTitle = "Join Event")
    }

    @Test
    fun eventDetailsView_lightTheme_data_primary_button_loading_state() {
        snapshotWithState(buttonLoading = true)
    }

    @Test
    fun eventDetailsView_lightTheme_event_full_user_not_joined() {
        snapshotWithState(
            currentAttendeeCount = 10,
            maxCapacity = 10,
            buttonTitle = "Full",
            isJoinButtonEnabled = false,
            showViewAttendeesButton = true
        )
    }

    @Test
    fun eventDetailsView_lightTheme_event_full_user_joined() {
        snapshotWithState(
            currentAttendeeCount = 10,
            maxCapacity = 10,
            buttonTitle = "Leave Event",
            isJoinButtonEnabled = true,
            showViewAttendeesButton = true
        )
    }

    @Test
    fun eventDetailsView_lightTheme_event_not_full_user_not_joined() {
        snapshotWithState(
            currentAttendeeCount = 4,
            maxCapacity = 10,
            buttonTitle = "Join Event",
            isJoinButtonEnabled = true,
            showViewAttendeesButton = true
        )
    }

    @Test
    fun eventDetailsView_lightTheme_event_empty_user_not_joined() {
        snapshotWithState(
            currentAttendeeCount = 0,
            maxCapacity = 10,
            buttonTitle = "Join Event",
            isJoinButtonEnabled = true,
            showViewAttendeesButton = false
        )
    }

    @Test
    fun eventDetailsView_lightTheme_event_no_max_capacity_user_joined() {
        snapshotWithState(
            currentAttendeeCount = 10,
            buttonTitle = "Join Event",
            isJoinButtonEnabled = true,
            showViewAttendeesButton = true
        )
    }

    private fun snapshotWithState(
        buttonTitle: String = "Sign in to sign up",
        buttonLoading: Boolean = false,
        showNavigateButton: Boolean = true,
        currentAttendeeCount: Int? = null,
        maxCapacity: Int? = null,
        isJoinButtonEnabled: Boolean = true,
        joinDeadline: String = "Fri, Sep 19, 2025, 20:00",
        showViewAttendeesButton: Boolean = false
    ) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventDetailsView(
                    uiState = EventDetailsUiState(
                        id = "1",
                        title = "Mobgen fest",
                        date = "Fri, Sep 26, 2025, 16:44",
                        description = "El mejor evento del año",
                        image = "test.png",
                        locationName = "Mi casa",
                        showNavigateButton = showNavigateButton,
                        primaryButtonTitle = buttonTitle,
                        isPrimaryButtonLoading = buttonLoading,
                        currentAttendeeCountText = maxCapacity?.let{ "$currentAttendeeCount/$maxCapacity attendees" },
                        isPrimaryButtonEnabled = isJoinButtonEnabled,
                        joinDeadline = joinDeadline,
                        showViewAttendeesButton = showViewAttendeesButton
                    ),
                    snackbarHostState = SnackbarHostState(),
                    onNavigateClicked = {},
                    onBackPressed = {},
                    onPrimaryButtonClicked = {},
                    onViewAttendeesClicked = {}
                )
            }
        }
    }

}