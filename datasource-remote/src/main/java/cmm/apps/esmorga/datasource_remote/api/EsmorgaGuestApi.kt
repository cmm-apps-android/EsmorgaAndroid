package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.event.model.EventListWrapperRemoteModel
import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EsmorgaGuestApi {

    @GET("events")
    suspend fun getEvents(): EventListWrapperRemoteModel
}