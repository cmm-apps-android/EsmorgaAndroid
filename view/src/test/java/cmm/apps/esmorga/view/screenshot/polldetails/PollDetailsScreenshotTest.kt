package cmm.apps.esmorga.view.screenshot.polldetails

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.polldetails.PollDetailsView
import cmm.apps.esmorga.view.polldetails.model.PollDetailsUiState
import cmm.apps.esmorga.view.polldetails.model.PollOptionUiModel
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module

class PollDetailsScreenshotTest : BaseScreenshotTest() {
    @Before
    fun setup() {
        startKoin {
            modules(
                module {
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
    fun pollDetailsView_lightTheme_multiple_choice() {
        snapshotWithState(isMultipleChoice = true)
    }

    @Test
    fun pollDetailsView_lightTheme_single_choice() {
        snapshotWithState(isMultipleChoice = false)
    }

    @Test
    fun pollDetailsView_lightTheme_primary_button_loading_state() {
        snapshotWithState(isButtonLoading = true)
    }

    @Test
    fun pollDetailsView_lightTheme_vote_button_disabled() {
        snapshotWithState(isButtonEnabled = false)
    }

    private fun snapshotWithState(
        isMultipleChoice: Boolean = true,
        isButtonLoading: Boolean = false,
        isButtonEnabled: Boolean = true
    ) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                PollDetailsView(
                    uiState = PollDetailsUiState(
                        id = "1",
                        title = "Pregunta muy seria",
                        voteDeadline = "Fri, Sep 26, 2025, 16:44",
                        description = "Encuesta super importante. Elige tu opción preferida:",
                        image = "test.png",
                        primaryButtonTitle = "Vota",
                        isPrimaryButtonLoading = isButtonLoading,
                        isPrimaryButtonEnabled = isButtonEnabled,
                        isMultipleChoice = isMultipleChoice,
                        options = listOf(
                            PollOptionUiModel(
                                id = "1",
                                text = "Opción A (7 votos)",
                                isSelected = true
                            ),
                            PollOptionUiModel(
                                id = "1",
                                text = "Opción B (11 votos)",
                                isSelected = false
                            ),
                        )
                    ),
                    snackbarHostState = SnackbarHostState(),
                    onOptionSelected = { _, _ -> },
                    onPrimaryButtonClicked = {},
                    onBackPressed = {},
                )
            }
        }
    }

}