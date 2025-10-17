package cmm.apps.esmorga.datasource_remote.dateformatting

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source

interface EsmorgaRemoteDateFormatter {
    fun parseIsoDateTime(dateString: String): ZonedDateTime
}

class RemoteDateFormatterImpl : EsmorgaRemoteDateFormatter {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSVV")

    override fun parseIsoDateTime(dateString: String): ZonedDateTime {
        return try {
            ZonedDateTime.from(formatter.parse(dateString))
        } catch (e: Exception) {
            throw EsmorgaException(
                message = "Error parsing remote date: $dateString",
                source = Source.REMOTE,
                code = ErrorCodes.PARSE_ERROR,
            )
        }
    }
}