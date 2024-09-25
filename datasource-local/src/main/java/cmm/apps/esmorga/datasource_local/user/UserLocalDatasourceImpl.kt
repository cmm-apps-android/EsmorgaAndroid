package cmm.apps.esmorga.datasource_local.user

import android.content.SharedPreferences
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.database.dao.UserDao
import cmm.apps.esmorga.datasource_local.user.mapper.toUserDataModel
import cmm.apps.esmorga.datasource_local.user.mapper.toUserLocalModel
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source

class UserLocalDatasourceImpl(private val userDao: UserDao, private val sharedPreferences: SharedPreferences) : UserDatasource {

    override suspend fun saveUser(user: UserDataModel) {
        sharedPreferences.edit().run {
            putString("access_token", user.dataAccessToken)
            putString("refresh_token",  user.dataRefreshToken)
        }.apply()
        userDao.insertUser(user.toUserLocalModel())
    }

    override suspend fun getUser(): UserDataModel {
        val user = userDao.getUser()
        user?.let {
            val accessToken = sharedPreferences.getString("access_token", null)
            val refreshToken = sharedPreferences.getString("refresh_token", null)
            return it.toUserDataModel(accessToken, refreshToken)
        } ?: throw EsmorgaException(message = "User not found", source = Source.LOCAL, code = ErrorCodes.NO_DATA)
    }
}