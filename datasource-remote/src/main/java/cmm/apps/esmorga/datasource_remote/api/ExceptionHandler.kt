package cmm.apps.esmorga.datasource_remote.api

import android.content.Context
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.datasource_remote.util.ConnectivityUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import java.time.format.DateTimeParseException

object ExceptionHandler {

    fun manageApiException(e: Exception, context: Context): EsmorgaException {
        return when (e) {
            is HttpException -> {
                FirebaseCrashlytics.getInstance().recordException(e)
                EsmorgaException(
                    message = e.response()?.message().orEmpty(),
                    source = Source.REMOTE,
                    code = e.code()
                )
            }

            is DateTimeParseException -> {
                FirebaseCrashlytics.getInstance().recordException(e)
                EsmorgaException(
                    message = "Date parse error: ${e.message.orEmpty()}",
                    source = Source.REMOTE,
                    code = ErrorCodes.PARSE_ERROR
                )
            }

            is ConnectException,
            is UnknownHostException -> {
                ConnectivityUtils.reportNoConnectivityIfNeeded(context)
                EsmorgaException(
                    message = "No connection error: ${e.message.orEmpty()}",
                    source = Source.REMOTE,
                    code = ErrorCodes.NO_CONNECTION
                )
            }

            is EsmorgaException -> e

            else -> {
                FirebaseCrashlytics.getInstance().recordException(e)
                EsmorgaException(
                    message = "Unexpected error: ${e.message.orEmpty()}",
                    source = Source.REMOTE,
                    code = ErrorCodes.UNKNOWN_ERROR
                )
            }
        }
    }
}