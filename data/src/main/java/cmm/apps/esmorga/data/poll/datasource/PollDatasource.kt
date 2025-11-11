package cmm.apps.esmorga.data.poll.datasource

import cmm.apps.esmorga.data.poll.model.PollDataModel


interface PollDatasource {
    suspend fun getPolls(): List<PollDataModel>
}