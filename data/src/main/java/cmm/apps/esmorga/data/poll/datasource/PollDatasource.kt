package cmm.apps.esmorga.data.poll.datasource

import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source


interface PollDatasource {
    suspend fun getPolls(): List<PollDataModel>

    suspend fun votePoll(pollId: String, options: List<String>): PollDataModel {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    suspend fun cachePolls(polls: List<PollDataModel>) {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    suspend fun deleteCachePolls() {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
}