package cmm.apps.esmorga.datasource_local.device

import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class DeviceLocalDataSourceImplTest {

    private val buildConfig: EsmorgaBuildConfig = mockk(relaxed = true)

    @Test
    fun `given device datasource in qa environment when get the build type then returns qa`() {
        val sut = DeviceLocalDataSourceImpl(buildConfig)

        coEvery { buildConfig.getEnvironment() } returns EsmorgaBuildConfig.Environment.QA

        val result = sut.getEnvironment()

        Assert.assertEquals(buildConfig.getEnvironment(), result)
    }

    @Test
    fun `given device datasource in prod environment when get the build type then returns prod`() {
        val sut = DeviceLocalDataSourceImpl(buildConfig)

        coEvery { buildConfig.getEnvironment() } returns EsmorgaBuildConfig.Environment.PROD

        val result = sut.getEnvironment()

        Assert.assertEquals(buildConfig.getEnvironment(), result)
    }
}