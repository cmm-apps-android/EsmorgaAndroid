package cmm.apps.esmorga.datasource_remote.user.model

data class UserRemoteModel(
    val accessToken: String,
    val refreshToken: String,
    val profile: ProfileRemoteModel
)

data class ProfileRemoteModel(
    val name: String,
    val lastName: String,
    val email: String
)
