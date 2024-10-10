package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class JoinEventUseCaseTest {

    @Test
    fun `given a successful repository when join event requested then return success`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.joinEvent(any()) } returns Unit

        val sut = JoinEventUseCaseImpl(repo)
        val result = sut.invoke("")

        Assert.assertEquals(EsmorgaResult.success(Unit), result)
    }
}