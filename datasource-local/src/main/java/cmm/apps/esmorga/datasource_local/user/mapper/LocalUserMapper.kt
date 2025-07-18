package cmm.apps.esmorga.datasource_local.user.mapper

import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.user.model.UserLocalModel
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.model.RoleType

fun UserLocalModel.toUserDataModel(accessToken: String?, refreshToken: String?): UserDataModel {
    val parsedRoleType = try {
        RoleType.valueOf(this.localRole)
    } catch (e: Exception) {
        throw EsmorgaException(message = "Error parsing role type [${this.localRole.uppercase()}] in UserLocalModel", source = Source.LOCAL, code = ErrorCodes.PARSE_ERROR)
    }
    return UserDataModel(
        dataEmail = localEmail,
        dataName = localName,
        dataLastName = localLastName,
        dataAccessToken = accessToken,
        dataRefreshToken = refreshToken,
        dataRole = parsedRoleType
    )
}

fun UserDataModel.toUserLocalModel(): UserLocalModel = UserLocalModel(
    localEmail = dataEmail,
    localName = dataName,
    localLastName = dataLastName,
    localRole = dataRole.name
)