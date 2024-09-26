package cmm.apps.esmorga.datasource_remote.user.model

import com.google.gson.annotations.SerializedName

data class UserRemoteModel(
    @SerializedName("accessToken") val remoteAccessToken: String,
    @SerializedName("refreshToken") val remoteRefreshToken: String,
    @SerializedName("ttl") val ttl: Int,
    @SerializedName("profile") val remoteProfile: ProfileRemoteModel
)

data class ProfileRemoteModel(
    @SerializedName("name") val remoteName: String,
    @SerializedName("lastName") val remoteLastName: String,
    @SerializedName("email") val remoteEmail: String
)
