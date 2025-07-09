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
    fun `returns true when buildType is qa`() = runTest {
        coEvery { repo.getBuildType() } returns "qa"

        val result = useCase()

        assertEquals(EsmorgaResult.success(true), result)
    }

    @Test
    fun `returns true when buildType is QA ignoring case`() = runTest {
        coEvery { repo.getBuildType() } returns "QA"

        val result = useCase()

        assertEquals(EsmorgaResult.success(true), result)
    }

    @Test
    fun `returns false when buildType is not qa`() = runTest {
        coEvery { repo.getBuildType() } returns "prod"

        val result = useCase()

        assertEquals(EsmorgaResult.success(false), result)
    }

    @Test
    fun `returns false when buildType is empty`() = runTest {
        coEvery { repo.getBuildType() } returns ""

        val result = useCase()

        assertEquals(EsmorgaResult.success(false), result)
    }
}
