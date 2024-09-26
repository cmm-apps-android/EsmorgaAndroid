package cmm.apps.esmorga.datasource_remote.api

import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import java.time.format.DateTimeParseException


object ExceptionHandler {

    fun manageApiException(e: Exception): EsmorgaException = when (e) {
        is HttpException -> EsmorgaException(message = e.response()?.message().orEmpty(), source = Source.REMOTE, code = e.code())
        is DateTimeParseException -> EsmorgaException(message = "Date parse error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.PARSE_ERROR)
        is ConnectException,
        is UnknownHostException -> EsmorgaException(message = "No connection error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.NO_CONNECTION)
        is EsmorgaException -> e
        else -> EsmorgaException(message = "Unexpected error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.UNKNOWN_ERROR)
    }

}