package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel
import retrofit2.http.Body
import retrofit2.http.POST

interface EsmorgaAuthApi {

    @POST("account/login")
    suspend fun login(@Body body: Map<String, String>): UserRemoteModel

    @POST("account/register")
    suspend fun register(@Body body: Map<String, String>): UserRemoteModel

    @POST("account/refresh")
    suspend fun refreshAccessToken(@Body body: Map<String, String>): UserRemoteModel

}