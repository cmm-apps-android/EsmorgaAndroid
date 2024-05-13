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

    @GET("94b88828-63ac-4a41-9872-073cdffa23ee")
    suspend fun getEvents(): EventListWrapperRemoteModel

}