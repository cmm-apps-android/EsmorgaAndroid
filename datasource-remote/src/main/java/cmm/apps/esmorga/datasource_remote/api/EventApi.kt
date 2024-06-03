package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.event.model.EventListWrapperRemoteModel
import retrofit2.http.GET


interface EventApi {

    companion object {
        fun baseUrl(): String = "https://qa.esmorga.canarte.org/v1/"
    }

    @GET("events")
    suspend fun getEvents(): EventListWrapperRemoteModel

}