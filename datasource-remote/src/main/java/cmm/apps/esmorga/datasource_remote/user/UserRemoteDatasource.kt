package cmm.apps.esmorga.datasource_remote.user

import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.user.mapper.toUserDataModel
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import java.time.format.DateTimeParseException

class UserRemoteDatasourceImpl(private val api: EsmorgaApi) : UserDatasource {
    override suspend fun login(email: String, password: String): Result<UserDataModel> {
        try {
            val loginBody = mapOf("email" to email, "password" to password)
            val user = api.login(loginBody)
            return Result.success(user.toUserDataModel())
        } catch (e: Exception) {
            when (e) {
                is HttpException -> throw EsmorgaException(message = e.response()?.message().orEmpty(), source = Source.REMOTE, code = e.code())
                is DateTimeParseException -> throw EsmorgaException(message = "Date parse error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.PARSE_ERROR)
                is ConnectException,
                is UnknownHostException -> throw EsmorgaException(message = "No connection error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.NO_CONNECTION)
                is EsmorgaException -> throw e
                else -> throw EsmorgaException(message = "Unexpected error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.UNKNOWN_ERROR)
            }
        }
    }
}