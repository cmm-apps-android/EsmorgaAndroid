package cmm.apps.esmorga.datasource_local.poll

import cmm.apps.esmorga.datasource_local.database.dao.PollDao
import cmm.apps.esmorga.datasource_local.mock.PollLocalMock
import cmm.apps.esmorga.datasource_local.poll.mapper.toPollDataModelList
import cmm.apps.esmorga.datasource_local.poll.model.PollLocalModel
import cmm.apps.esmorga.datasource_local.poll.model.PollOptionLocalModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test

class EventLocalDatasourceImplTest {

    private val fakePollStorage = mutableListOf<String>()
    private val fakePollOptionStorage = mutableListOf<String>()

    private fun provideFakePollDao(): PollDao {
        val pollListSlot = slot<List<PollLocalModel>>()
        val pollOptionListSlot = slot<List<PollOptionLocalModel>>()
        val dao = mockk<PollDao>()
        coEvery { dao.getPolls() } coAnswers {
            mutableMapOf<PollLocalModel, List<PollOptionLocalModel>>().also {
                for (name in fakePollStorage) {
                    it[PollLocalMock.providePoll(name)] = fakePollOptionStorage.filter { it.startsWith(name) }.map { PollLocalMock.providePollOption(it) }
                }
            }
        }
        coEvery { dao.insertPolls(capture(pollListSlot)) } coAnswers {
            fakePollStorage.addAll(pollListSlot.captured.map { poll -> poll.localName })
        }
        coEvery { dao.insertPollOptions(capture(pollOptionListSlot)) } coAnswers {
            fakePollOptionStorage.addAll(pollOptionListSlot.captured.map { poll -> poll.localText })
        }
        coEvery { dao.deleteAllPolls() } coAnswers {
            fakePollStorage.clear()
        }
        coEvery { dao.deleteAllPollOptions() } coAnswers {
            fakePollOptionStorage
        }

        return dao
    }

    @After
    fun shutDown() {
        fakePollStorage.clear()
    }

    @Test
    fun `given a working dao when polls requested then polls successfully returned`() = runTest {
        val localPollName = "LocalPoll"

        val dao = mockk<PollDao>(relaxed = true)
        coEvery { dao.getPolls() } returns mapOf(PollLocalMock.providePoll(localPollName) to PollLocalMock.providePollOptionList(listOf(localPollName)))

        val sut = PollLocalDatasourceImpl(dao)
        val result = sut.getPolls()

        Assert.assertEquals(localPollName, result[0].dataName)
    }

    @Test
    fun `given an empty storage when polls cached then polls are stored successfully`() = runTest {
        val localPollName = "LocalPoll"

        val sut = PollLocalDatasourceImpl(provideFakePollDao())
        sut.cachePolls(PollLocalMock.providePollMap(listOf(localPollName)).toPollDataModelList())
        val result = sut.getPolls()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localPollName, result[0].dataName)
    }

    @Test
    fun `given a storage with polls when polls cached then old polls are removed and new polls are stored successfully`() = runTest {
        val localPollName = "LocalPoll"
        fakePollStorage.add("ShouldBeRemoved")
        fakePollOptionStorage.add("ShouldBeRemoved")

        val sut = PollLocalDatasourceImpl(provideFakePollDao())
        sut.cachePolls(PollLocalMock.providePollMap(listOf(localPollName)).toPollDataModelList())
        val result = sut.getPolls()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localPollName, result[0].dataName)
        Assert.assertEquals(localPollName, result[0].dataOptions[0].text)
    }

    @Test
    fun `given a storage with polls when delete cache is requested then the result is empty`() = runTest {
        val localPollName = "LocalPoll"

        val sut = PollLocalDatasourceImpl(provideFakePollDao())
        sut.cachePolls(PollLocalMock.providePollMap(listOf(localPollName)).toPollDataModelList())

        sut.deleteCachePolls()

        val polls = sut.getPolls()
        Assert.assertTrue("Poll list is not empty", polls.isEmpty())
    }

}