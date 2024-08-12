package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.event.model.EventListWrapperRemoteModel
import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface EsmorgaApi {

    companion object {
        fun baseUrl(): String = "https://qa.esmorga.canarte.org/v1/"
    }

    @GET("events")
    suspend fun getEvents(): EventListWrapperRemoteModel

    @POST("account/login")
    suspend fun login(@Body body: Map<String, String>): UserRemoteModel

    @POST("account/register")
    suspend fun register(@Body body: Map<String, String>): UserRemoteModel

}