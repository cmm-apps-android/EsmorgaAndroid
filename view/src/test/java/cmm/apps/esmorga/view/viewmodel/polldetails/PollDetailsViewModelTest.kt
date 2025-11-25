package cmm.apps.esmorga.view.viewmodel.polldetails

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.domain.poll.VotePollUseCase
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.polldetails.PollDetailsViewModel
import cmm.apps.esmorga.view.viewmodel.mock.PollViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class PollDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockContext: Context

    private val votePollUseCase = mockk<VotePollUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(PollViewMock.providePoll("Poll"))
    }

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
        startKoin {
            androidContext(mockContext)
            modules(module {
                single<EsmorgaDateTimeFormatter> { DateFormatterImpl() }
            })
        }
    }

    @After
    fun shutDown() {
        stopKoin()
    }

    @Test
    fun `given a poll and a user that did not vote when screen is opened then UI state containing poll is emitted`() = runTest {
        val pollName = "Poll Name"

        val sut = PollDetailsViewModel(votePollUseCase, PollViewMock.providePoll(pollName)).also {
            it.onCreate(mockk<LifecycleOwner>(relaxed = true))
        }

        val uiState = sut.uiState.value
        Assert.assertEquals(pollName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_poll_vote), uiState.primaryButtonTitle)
        Assert.assertFalse("Vote button should be disabled", uiState.isPrimaryButtonEnabled)
    }

    @Test
    fun `given a poll and a user that already voted when screen is opened then UI state containing poll is emitted`() = runTest {
        val pollName = "Poll Name"

        val sut = PollDetailsViewModel(votePollUseCase, PollViewMock.providePoll(name = pollName, options = mapOf("option" to true))).also {
            it.onCreate(mockk<LifecycleOwner>(relaxed = true))
        }

        val uiState = sut.uiState.value
        Assert.assertEquals(pollName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_poll_change_vote), uiState.primaryButtonTitle)
        Assert.assertFalse("Vote button should be disabled", uiState.isPrimaryButtonEnabled)
    }

    @Test
    fun `given a multiple choice poll when user selects an option then UI state containing enabled button is emitted`() = runTest {
        val pollName = "Poll Name"
        val optionId = "Option Id"

        val sut = PollDetailsViewModel(votePollUseCase, PollViewMock.providePoll(name = pollName, options = mapOf(optionId to false))).also {
            it.onCreate(mockk<LifecycleOwner>(relaxed = true))
        }

        sut.onOptionSelected(optionId, true)

        val uiState = sut.uiState.value
        Assert.assertEquals(pollName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_poll_vote), uiState.primaryButtonTitle)
        Assert.assertTrue("Vote button should be enabled", uiState.isPrimaryButtonEnabled)
    }

    @Test
    fun `given a multiple choice poll when user votes then UI state containing disabled button is emitted`() = runTest {
        val pollName = "Poll Name"
        val optionId = "Option Id"
        val poll = PollViewMock.providePoll(name = pollName, options = mapOf(optionId to false))

        val votePollUseCase = mockk<VotePollUseCase>(relaxed = true).also { useCase ->
            coEvery { useCase(any(), any()) } returns EsmorgaResult.success(PollViewMock.providePoll(name = pollName, options = mapOf(optionId to true)))
        }

        val sut = PollDetailsViewModel(votePollUseCase, poll).also {
            it.onCreate(mockk<LifecycleOwner>(relaxed = true))
        }

        sut.onOptionSelected(optionId, true)
        sut.onPrimaryButtonClicked()

        val uiState = sut.uiState.value
        Assert.assertEquals(pollName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_poll_change_vote), uiState.primaryButtonTitle)
        Assert.assertFalse("Vote button should be disabled", uiState.isPrimaryButtonEnabled)
    }

    @Test
    fun `given a single choice poll when user selects an option then UI state reflects all other options disabled`() = runTest {
        val pollName = "Poll Name"
        val selectedOptionId = "Selected Id"
        val notSelectedOptionId = "Not Selected Id"

        val sut = PollDetailsViewModel(
            votePollUseCase,
            PollViewMock.providePoll(name = pollName, isMultipleChoice = false, options = mapOf(selectedOptionId to true, notSelectedOptionId to false))
        ).also {
            it.onCreate(mockk<LifecycleOwner>(relaxed = true))
        }

        sut.onOptionSelected(notSelectedOptionId, true)

        val uiState = sut.uiState.value
        Assert.assertTrue("Option should be selected", uiState.options.find { it.id == notSelectedOptionId }?.isSelected == true)
        Assert.assertTrue("Option should not be selected", uiState.options.find { it.id == selectedOptionId }?.isSelected == false)
    }

    @Test
    fun `given a multiple choice poll when user selects an option then UI state reflects only that options changed`() = runTest {
        val pollName = "Poll Name"
        val option1 = "Option 1"
        val option2 = "Option 2"

        val sut = PollDetailsViewModel(
            votePollUseCase,
            PollViewMock.providePoll(name = pollName, isMultipleChoice = true, options = mapOf(option1 to false, option2 to true))
        ).also {
            it.onCreate(mockk<LifecycleOwner>(relaxed = true))
        }

        sut.onOptionSelected(option1, true)

        val uiState = sut.uiState.value
        Assert.assertTrue("Option should be selected", uiState.options.find { it.id == option1 }?.isSelected == true)
        Assert.assertTrue("Option should be selected", uiState.options.find { it.id == option2 }?.isSelected == true)
    }

    @Test
    fun `given a poll with deadline passed when screen is opened then UI state with button disabled is emitted`() = runTest {
        val sut = PollDetailsViewModel(
            votePollUseCase,
            PollViewMock.providePoll(name = "poll").copy(voteDeadline = System.currentTimeMillis() - 10000)
        ).also {
            it.onCreate(mockk<LifecycleOwner>(relaxed = true))
        }

        val uiState = sut.uiState.value
        Assert.assertEquals(mockContext.getString(R.string.button_deadline_passed), uiState.primaryButtonTitle)
        Assert.assertFalse("Option should be selected", uiState.isPrimaryButtonEnabled)
    }

}