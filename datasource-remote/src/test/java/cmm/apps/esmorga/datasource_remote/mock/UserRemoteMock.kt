package cmm.apps.esmorga.datasource_remote.mock

import cmm.apps.esmorga.datasource_remote.user.model.ProfileRemoteModel
import cmm.apps.esmorga.datasource_remote.user.model.UserRemoteModel

object UserRemoteMock {

    fun provideUser(name: String): UserRemoteModel = UserRemoteModel(
        remoteProfile = ProfileRemoteModel(
            remoteName = name,
            remoteEmail = "$",
            remoteLastName = "Doe"
        ),
        remoteRefreshToken = "refreshToken",
        remoteAccessToken = "token",
    )
}