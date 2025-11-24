package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.poll.model.PollListWrapperRemoteModel
import cmm.apps.esmorga.datasource_remote.poll.model.PollRemoteModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface EsmorgaPollApi {

    @GET("polls")
    suspend fun getPolls(): PollListWrapperRemoteModel

    @POST("polls/{pollId}/vote")
    suspend fun votePoll(@Path("pollId") pollId: String, @Body body: Map<String, @JvmSuppressWildcards List<String>>): PollRemoteModel

}