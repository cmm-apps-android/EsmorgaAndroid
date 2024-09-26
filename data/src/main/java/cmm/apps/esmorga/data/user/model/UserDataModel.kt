package cmm.apps.esmorga.data.user.model

data class UserDataModel(
    val dataName: String,
    val dataLastName: String,
    val dataEmail: String,
    val dataAccessToken: String?,
    val dataRefreshToken: String?
)