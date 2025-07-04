package cmm.apps.esmorga.domain.device

import cmm.apps.esmorga.domain.device.repository.DeviceRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetDeviceIdUseCaseTest {

    @Test
    fun `given a device id when getDeviceId is called then device id is returned`() = runTest {
        val expectedDeviceId = "24601"

        val repo = mockk<DeviceRepository>(relaxed = true)
        coEvery { repo.getDeviceId() } returns expectedDeviceId

        val sut = GetDeviceIdUseCaseImpl(repo)
        val result = sut.invoke()

        Assert.assertEquals(expectedDeviceId, result.data)
    }
}