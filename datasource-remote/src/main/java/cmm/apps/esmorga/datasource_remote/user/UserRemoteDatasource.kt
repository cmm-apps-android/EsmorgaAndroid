package cmm.apps.esmorga.datasource_remote.user

import android.content.Context
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.api.ExceptionHandler.manageApiException
import cmm.apps.esmorga.datasource_remote.user.mapper.toUserDataModel

class UserRemoteDatasourceImpl(private val api: EsmorgaAuthApi, private val context: Context) : UserDatasource {
    override suspend fun login(email: String, password: String): UserDataModel {
        try {
            val loginBody = mapOf("email" to email, "password" to password)
            val user = api.login(loginBody)
            return user.toUserDataModel()
        } catch (e: Exception) {
            throw manageApiException(e, context)
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
            api.register(registerBody)
        } catch (e: Exception) {
            throw manageApiException(e, context)
        }
    }

    override suspend fun emailVerification(email: String) {
        try {
            val emailBody = mapOf(
                "email" to email
            )
            api.emailVerification(emailBody)
        } catch (e: Exception) {
            throw manageApiException(e, context)
        }
    }

    override suspend fun activateAccount(verificationCode: String): UserDataModel {
        try {
            val body = mapOf("verificationCode" to verificationCode)
            return api.accountActivation(body).toUserDataModel()
        } catch (e: Exception) {
            throw manageApiException(e, context)
        }
    }

    override suspend fun recoverPassword(email: String) {
        try {
            val emailBody = mapOf(
                "email" to email
            )
            api.recoverPassword(emailBody)
        } catch (e: Exception) {
            throw manageApiException(e, context)
        }
    }
}