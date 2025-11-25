package cmm.apps.esmorga.datasource_remote.poll

import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.datasource_remote.api.EsmorgaPollApi
import cmm.apps.esmorga.datasource_remote.api.ExceptionHandler.manageApiException
import cmm.apps.esmorga.datasource_remote.dateformatting.EsmorgaRemoteDateFormatter
import cmm.apps.esmorga.datasource_remote.poll.mapper.toPollDataModel
import cmm.apps.esmorga.datasource_remote.poll.mapper.toPollDataModelList
import org.json.JSONArray


class PollRemoteDatasourceImpl(private val pollApi: EsmorgaPollApi, private val dateFormatter: EsmorgaRemoteDateFormatter) : PollDatasource {

    override suspend fun getPolls(): List<PollDataModel> {
        try {
            val pollList = pollApi.getPolls()
            return pollList.remotePollList.toPollDataModelList(dateFormatter)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun votePoll(pollId: String, options: List<String>): PollDataModel {
        try {
            val poll = pollApi.votePoll(pollId, mapOf("selectedOptions" to options))
            return poll.toPollDataModel(dateFormatter)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }
}