package cmm.apps.esmorga.data.user.datasource

import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source

interface AuthDatasource {
    suspend fun refreshTokens(refreshToken: String): String? {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
}