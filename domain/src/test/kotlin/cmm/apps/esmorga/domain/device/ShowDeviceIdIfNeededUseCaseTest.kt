package cmm.apps.esmorga.domain.device

import cmm.apps.esmorga.domain.device.repository.DeviceRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ShowDeviceIdIfNeededUseCaseTest {

    private val repo: DeviceRepository = mockk()
    private val useCase = ShowDeviceIdIfNeededUseCaseImpl(repo)

    @Test
    fun `given buildType is qa when invoke then returns true`() = runTest {
        coEvery { repo.getBuildType() } returns "qa"

        val result = useCase()

        assertEquals(EsmorgaResult.success(true), result)
    }

    @Test
    fun `given buildType is QA when invoke then returns true ignoring case`() = runTest {
        coEvery { repo.getBuildType() } returns "QA"

        val result = useCase()

        assertEquals(EsmorgaResult.success(true), result)
    }

    @Test
    fun `given buildType is prod when invoke then returns false`() = runTest {
        coEvery { repo.getBuildType() } returns "prod"

        val result = useCase()

        assertEquals(EsmorgaResult.success(false), result)
    }

    @Test
    fun `given buildType is empty when invoke then returns false`() = runTest {
        coEvery { repo.getBuildType() } returns ""

        val result = useCase()

        assertEquals(EsmorgaResult.success(false), result)
    }
}
