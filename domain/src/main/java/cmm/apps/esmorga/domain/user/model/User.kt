package cmm.apps.esmorga.domain.user.model

data class User(
    val name: String,
    val lastName: String,
    val email: String
) {
    companion object {
        const val NAME_REGEX = "^[a-zA-Z '\\-]{3,100}$"
        const val EMAIL_REGEX = "^(?!.{101})[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        const val PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+'\\-=+/<>]).{8,50}$"
    }
}