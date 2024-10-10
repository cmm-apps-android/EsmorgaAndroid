package cmm.apps.esmorga.datasource_remote.user

import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.api.ExceptionHandler.manageApiException
import cmm.apps.esmorga.datasource_remote.user.mapper.toUserDataModel

class UserRemoteDatasourceImpl(private val api: EsmorgaAuthApi) : UserDatasource {
    override suspend fun login(email: String, password: String): UserDataModel {
        try {
            val loginBody = mapOf("email" to email, "password" to password)
            val user = api.login(loginBody)
            return user.toUserDataModel()
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun register(name: String, lastName: String, email: String, password: String): UserDataModel {
        try {
            val registerBody = mapOf(
                "name" to name,
                "lastName" to lastName,
                "email" to email,
                "password" to password
            )
            val user = api.register(registerBody)
            return user.toUserDataModel()
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

}