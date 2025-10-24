package cmm.apps.esmorga.view.viewmodel.eventattendees

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.domain.event.GetEventAttendeesUseCase
import cmm.apps.esmorga.domain.event.UpdateEventAttendeeUseCase
import cmm.apps.esmorga.domain.result.EsmorgaResult
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

        val updateEventAttendeeUseCase = mockk<UpdateEventAttendeeUseCase>(relaxed = true)

        val sut = EventAttendeesViewModel(getAttendeesUseCase, updateEventAttendeeUseCase, "EventId")
        sut.onStart(mockk<LifecycleOwner>(relaxed = true))

        val uiState = sut.uiState.value
        Assert.assertEquals("1. $domainAttendeeName", uiState.attendeeList[0].name)
    }

    @Test
    fun `given a successful usecase when attendee checked usecase is executed`() = runTest {
        val domainAttendeeName = "DomainAttendee"

        val getAttendeesUseCase = mockk<GetEventAttendeesUseCase>(relaxed = true)
        coEvery { getAttendeesUseCase(any()) } returns EsmorgaResult.success(EventAttendeeViewMock.provideEventAttendeeList(listOf(domainAttendeeName)))
        val updateEventAttendeeUseCase = mockk<UpdateEventAttendeeUseCase>(relaxed = true)

        val sut = EventAttendeesViewModel(getAttendeesUseCase, updateEventAttendeeUseCase, "EventId")
        sut.onStart(mockk<LifecycleOwner>(relaxed = true))
        sut.onAttendeeChecked(0, true)

        coVerify { updateEventAttendeeUseCase(any()) }
    }
}