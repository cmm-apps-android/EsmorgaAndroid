package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface EsmorgaAccountAuthenticatedApi {

    @PUT("account/password")
    suspend fun changePassword(@Body body: Map<String, String>)
}