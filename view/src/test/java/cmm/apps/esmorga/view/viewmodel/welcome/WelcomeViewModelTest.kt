package cmm.apps.esmorga.view.viewmodel.welcome

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.device.GetDeviceIdUseCase
import cmm.apps.esmorga.domain.device.ShowDeviceIdIfNeededUseCase
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import cmm.apps.esmorga.view.welcome.WelcomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
class WelcomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getDeviceIdUseCase: GetDeviceIdUseCase = mockk()
    private val showDeviceIdNeededUseCase: ShowDeviceIdIfNeededUseCase = mockk()

    @Before
    fun setup() {
        val mockContext = ApplicationProvider.getApplicationContext<Context>()
        startKoin {
            androidContext(mockContext)
        }
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `given a success usecase, when is qa envoriment then deviceId is displayed`() = runTest {
        coEvery { showDeviceIdNeededUseCase() } returns EsmorgaResult.success(true)
        coEvery { getDeviceIdUseCase() } returns EsmorgaResult.success("01234")

        val viewModel = WelcomeViewModel(getDeviceIdUseCase, showDeviceIdNeededUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("01234", state.deviceId)
        }
    }

    @Test
    fun `given a success usecase when is prod environment then deviceId is not displayed`() = runTest {
        coEvery { showDeviceIdNeededUseCase() } returns EsmorgaResult.success(false)
        coEvery { getDeviceIdUseCase() } returns EsmorgaResult.success("01234")

        val viewModel = WelcomeViewModel(getDeviceIdUseCase, showDeviceIdNeededUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.deviceId)
        }
    }
}