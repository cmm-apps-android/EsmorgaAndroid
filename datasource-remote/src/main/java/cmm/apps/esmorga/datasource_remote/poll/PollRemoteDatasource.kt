package cmm.apps.esmorga.datasource_remote.poll

import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventApi
import cmm.apps.esmorga.datasource_remote.api.ExceptionHandler.manageApiException
import cmm.apps.esmorga.datasource_remote.dateformatting.EsmorgaRemoteDateFormatter
import cmm.apps.esmorga.datasource_remote.poll.mapper.toPollDataModelList


class PollRemoteDatasourceImpl(private val eventApi: EsmorgaEventApi, private val dateFormatter: EsmorgaRemoteDateFormatter) : PollDatasource {

    override suspend fun getPolls(): List<PollDataModel> {
        try {
            val pollList = eventApi.getPolls()
            return pollList.remotePollList.toPollDataModelList(dateFormatter)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }
}