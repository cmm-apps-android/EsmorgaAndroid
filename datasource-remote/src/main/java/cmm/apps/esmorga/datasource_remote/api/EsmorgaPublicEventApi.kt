package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.event.model.EventListWrapperRemoteModel
import retrofit2.http.GET

interface EsmorgaPublicEventApi {

    @GET("events")
    suspend fun getEvents(): EventListWrapperRemoteModel
}