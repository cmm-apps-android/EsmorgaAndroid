package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface EsmorgaAuthApi {

    @POST("account/login")
    suspend fun login(@Body body: Map<String, String>): UserRemoteModel

    @POST("account/register")
    suspend fun register(@Body body: Map<String, String>)

    @POST("account/refresh")
    suspend fun refreshAccessToken(@Body body: Map<String, String>): UserRemoteModel

    @POST("account/email/verification")
    suspend fun emailVerification(@Body body: Map<String, String>)

    @PUT("account/activate")
    suspend fun accountActivation(@Body body: Map<String, String>)

    @POST("account/password/forgot-init")
    suspend fun recoverPassword(@Body body: Map<String, String>)
}