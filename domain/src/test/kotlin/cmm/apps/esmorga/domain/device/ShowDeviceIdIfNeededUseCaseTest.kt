package cmm.apps.esmorga.domain.device

import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig
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
    fun `given qa environment when get environment is called then returns true`() = runTest {
        val buildConfig: EsmorgaBuildConfig = mockk()
        coEvery { buildConfig.getEnvironment() } returns EsmorgaBuildConfig.Environment.QA
        coEvery { repo.getEnvironment() } returns EsmorgaBuildConfig.Environment.QA

        val result = useCase()

        assertEquals(EsmorgaResult.success(true), result)
    }

    @Test
    fun `given prod environment when get environment is called then returns false`() = runTest {
        val buildConfig: EsmorgaBuildConfig = mockk()
        coEvery { buildConfig.getEnvironment() } returns EsmorgaBuildConfig.Environment.PROD
        coEvery { repo.getEnvironment() } returns EsmorgaBuildConfig.Environment.PROD

        val result = useCase()

        assertEquals(EsmorgaResult.success(false), result)
    }
}
