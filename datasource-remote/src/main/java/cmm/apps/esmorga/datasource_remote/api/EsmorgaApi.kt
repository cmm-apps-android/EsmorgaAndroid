package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.event.model.EventListWrapperRemoteModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface EsmorgaApi {

    @GET("account/events")
    suspend fun getMyEvents(): EventListWrapperRemoteModel

    @POST("account/events")
    suspend fun joinEvent(@Body body: Map<String, String>)
}