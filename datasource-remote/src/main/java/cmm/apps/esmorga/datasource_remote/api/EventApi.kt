package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.EventListWrapperRemoteModel
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface EventApi {

    companion object {
        fun baseUrl(): String = "https://run.mocky.io/v3/"
    }

    @GET("001f8cdc-39c5-4c6d-9402-da0451d5c5c8")
    suspend fun getEvents(): EventListWrapperRemoteModel

}