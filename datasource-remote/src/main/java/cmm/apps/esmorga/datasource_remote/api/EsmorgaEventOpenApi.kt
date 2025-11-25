package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.event.model.EventListWrapperRemoteModel
import retrofit2.http.GET

interface EsmorgaEventOpenApi {

    @GET("events")
    suspend fun getEvents(): EventListWrapperRemoteModel
}