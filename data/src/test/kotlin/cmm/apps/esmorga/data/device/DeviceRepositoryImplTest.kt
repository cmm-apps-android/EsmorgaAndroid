package cmm.apps.esmorga.data.device

import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class DeviceRepositoryImplTest {

    @Test
    fun `given a device id when getDeviceId is called then device id is returned`() {
        val expectedDeviceId = "24601"

        val localDS = mockk<DeviceDataSource>(relaxed = true)
        val remoteDS = mockk<DeviceDataSource>(relaxed = true)
        coEvery { remoteDS.getDeviceId() } returns expectedDeviceId

        val sut = DeviceRepositoryImpl(remoteDS, localDS)
        val result = sut.getDeviceId()

        Assert.assertEquals(expectedDeviceId, result)
    }
}