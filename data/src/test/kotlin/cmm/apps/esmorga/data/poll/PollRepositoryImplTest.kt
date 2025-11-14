package cmm.apps.esmorga.data.poll

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.mock.PollDataMock
import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PollRepositoryImplTest {

    private lateinit var localDS: PollDatasource
    private lateinit var remoteDS: PollDatasource

    @Before
    fun setUp() {
        localDS = mockk<PollDatasource>(relaxed = true)
        remoteDS = mockk<PollDatasource>(relaxed = true)
    }

    @Test
    fun `given empty local when polls requested then remote polls are cached and returned`() = runTest {
        val remoteName = "RemotePoll"

        coEvery { remoteDS.getPolls() } returns PollDataMock.providePollDataModelList(listOf(remoteName))

        val sut = PollRepositoryImpl(localDS, remoteDS)
        val result = sut.getPolls()

        Assert.assertEquals(remoteName, result[0].name)
        coVerify { localDS.cachePolls(any()) }
    }

    @Test
    fun `given non empty local and cache not expired when polls requested then local polls are returned`() = runTest {
        val localName = "LocalPoll"

        coEvery { localDS.getPolls() } returns PollDataMock.providePollDataModelList(listOf(localName))
        coEvery { remoteDS.getPolls() } returns PollDataMock.providePollDataModelList(listOf("RemotePoll"))

        val sut = PollRepositoryImpl(localDS, remoteDS)
        val result = sut.getPolls()

        Assert.assertEquals(localName, result[0].name)
    }

    @Test
    fun `given non empty local and cache expired when polls requested then remote polls are returned`() = runTest {
        val remoteName = "RemotePoll"
        val oldDate = System.currentTimeMillis() - (CacheHelper.DEFAULT_CACHE_TTL + 1000)

        coEvery { localDS.getPolls() } returns listOf(PollDataMock.providePollDataModel("LocalPoll").copy(dataCreationTime = oldDate))
        coEvery { remoteDS.getPolls() } returns PollDataMock.providePollDataModelList(listOf(remoteName))

        val sut = PollRepositoryImpl(localDS, remoteDS)
        val result = sut.getPolls()

        Assert.assertEquals(remoteName, result[0].name)
    }

    @Test(expected = EsmorgaException::class)
    fun `given no connection when polls requested then local polls are returned and a non blocking error is returned`() = runTest {
        val localName = "LocalPoll"
        val oldDate = System.currentTimeMillis() - (CacheHelper.DEFAULT_CACHE_TTL + 1000)

        coEvery { localDS.getPolls() } returns listOf(PollDataMock.providePollDataModel(localName).copy(dataCreationTime = oldDate))
        coEvery { remoteDS.getPolls() } throws EsmorgaException(message = "No connection", code = ErrorCodes.NO_CONNECTION, source = Source.REMOTE)

        val sut = PollRepositoryImpl( localDS, remoteDS)
        val result = sut.getPolls()

        Assert.assertEquals(localName, result[0].name)
    }

}