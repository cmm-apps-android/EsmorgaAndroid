package cmm.apps.esmorga.datasource_remote.user

import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAccountApi
import cmm.apps.esmorga.datasource_remote.api.ExceptionHandler.manageApiException
import cmm.apps.esmorga.datasource_remote.user.mapper.toUserDataModel

class UserRemoteDatasourceImpl(private val accountApi: EsmorgaAccountApi, private val eventsApi: EsmorgaEventApi, private val authDatasource: AuthDatasource) : UserDatasource {
    override suspend fun login(email: String, password: String): UserDataModel {
        try {
            val loginBody = mapOf("email" to email, "password" to password)
            val user = accountApi.login(loginBody)
            authDatasource.saveTokens(user.remoteAccessToken, user.remoteRefreshToken, user.ttl)
            return user.toUserDataModel()
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun register(name: String, lastName: String, email: String, password: String) {
        try {
            val registerBody = mapOf(
                "name" to name,
                "lastName" to lastName,
                "email" to email,
                "password" to password
            )
            accountApi.register(registerBody)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun emailVerification(email: String) {
        try {
            val emailBody = mapOf(
                "email" to email
            )
            accountApi.emailVerification(emailBody)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun activateAccount(verificationCode: String): UserDataModel {
        try {
            val body = mapOf("verificationCode" to verificationCode)
            val user = accountApi.accountActivation(body)
            authDatasource.saveTokens(user.remoteAccessToken, user.remoteRefreshToken, user.ttl)
            return user.toUserDataModel()
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun recoverPassword(email: String) {
        try {
            val emailBody = mapOf(
                "email" to email
            )
            accountApi.recoverPassword(emailBody)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun resetPassword(code: String, password: String) {
        try {
            val body = mapOf(
                "password" to password,
                "forgotPasswordCode" to code
            )
            accountApi.resetPassword(body)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun deleteUserSession() {
        authDatasource.deleteTokens()
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String) {
        try {
            val body = mapOf(
                "currentPassword" to currentPassword,
                "newPassword" to newPassword
            )
            accountApi.changePassword(body)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }
}