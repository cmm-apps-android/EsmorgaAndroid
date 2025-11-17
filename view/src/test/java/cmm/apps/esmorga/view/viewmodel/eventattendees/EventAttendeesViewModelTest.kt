package cmm.apps.esmorga.view.viewmodel.eventattendees

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.domain.event.GetEventAttendeesUseCase
import cmm.apps.esmorga.domain.event.UpdateEventAttendeeUseCase
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.model.RoleType
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.eventattendees.EventAttendeesViewModel
import cmm.apps.esmorga.view.viewmodel.mock.EventAttendeeViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
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


@RunWith(AndroidJUnit4::class)
class EventAttendeesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockContext: Context

    private val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(User("", "", "", RoleType.USER))
    }
    val updateEventAttendeeUseCase = mockk<UpdateEventAttendeeUseCase>(relaxed = true)

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
        startKoin {
            androidContext(mockContext)
        }
    }

    @After
    fun shutDown() {
        stopKoin()
    }

    @Test
    fun `given a successful usecase when load method is called usecase executed and UI state containing attendees is emitted`() = runTest {
        val domainAttendeeName = "DomainAttendee"

        val getAttendeesUseCase = mockk<GetEventAttendeesUseCase>(relaxed = true)
        coEvery { getAttendeesUseCase(any()) } returns EsmorgaResult.success(EventAttendeeViewMock.provideEventAttendeeList(listOf(domainAttendeeName)))

        val sut = EventAttendeesViewModel(getSavedUserUseCase, getAttendeesUseCase, updateEventAttendeeUseCase, "EventId")
        sut.onStart(mockk<LifecycleOwner>(relaxed = true))

        val uiState = sut.uiState.value
        Assert.assertEquals(domainAttendeeName, uiState.attendeeList[0].name)
        Assert.assertFalse("ShouldShowChecked should be false for normal users", uiState.showChecked)
    }

    @Test
    fun `given a successful usecase and an admin user when load method is called usecase executed and UI state containing attendees and showing checks is emitted`() = runTest {
        val domainAttendeeName = "DomainAttendee"

        val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { getSavedUserUseCase() } returns EsmorgaResult.success(User("Name", "LN", "e@ma.il", RoleType.ADMIN))

        val getAttendeesUseCase = mockk<GetEventAttendeesUseCase>(relaxed = true)
        coEvery { getAttendeesUseCase(any()) } returns EsmorgaResult.success(EventAttendeeViewMock.provideEventAttendeeList(listOf(domainAttendeeName)))

        val sut = EventAttendeesViewModel(getSavedUserUseCase, getAttendeesUseCase, updateEventAttendeeUseCase, "EventId")
        sut.onStart(mockk<LifecycleOwner>(relaxed = true))

        val uiState = sut.uiState.value
        Assert.assertEquals(domainAttendeeName, uiState.attendeeList[0].name)
        Assert.assertTrue("ShouldShowChecked should be true for ADMIN users", uiState.showChecked)
    }

    @Test
    fun `given a successful usecase when attendee checked usecase is executed`() = runTest {
        val domainAttendeeName = "DomainAttendee"

        val updateEventAttendeeUseCase = mockk<UpdateEventAttendeeUseCase>(relaxed = true)
        val getAttendeesUseCase = mockk<GetEventAttendeesUseCase>(relaxed = true)
        coEvery { getAttendeesUseCase(any()) } returns EsmorgaResult.success(EventAttendeeViewMock.provideEventAttendeeList(listOf(domainAttendeeName)))

        val sut = EventAttendeesViewModel(getSavedUserUseCase, getAttendeesUseCase, updateEventAttendeeUseCase, "EventId")
        sut.onStart(mockk<LifecycleOwner>(relaxed = true))
        sut.onAttendeeChecked(0, true)

        coVerify { updateEventAttendeeUseCase(any()) }
    }
}