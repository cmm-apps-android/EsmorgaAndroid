package cmm.apps.esmorga.datasource_remote.api.authenticator

object AuthorizationConstants {
    const val AUTHORIZATION_HEADER_KEY = "Authorization"
    const val AUTHORIZATION_HEADER_VALUE = "Bearer %s"
    const val REFRESH_TOKEN_KEY = "refreshToken"
    const val SHARED_PREFERENCES_KEY = "auth_prefs"
    const val SHARED_AUTH_TOKEN_KEY = "access_token"
    const val SHARED_REFRESH_TOKEN_KEY = "refresh_token"
}