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

class UserLocalDatasourceImpl(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) : UserDatasource {

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val TTL_KEY = "ttl"
    }

    override suspend fun saveUser(user: UserDataModel) {
        sharedPreferences.edit().run {
            putString(ACCESS_TOKEN_KEY, user.dataAccessToken)
            putString(REFRESH_TOKEN_KEY, user.dataRefreshToken)
            putLong(TTL_KEY, user.dataTtl * 1000 + System.currentTimeMillis())
            apply()
        }
        userDao.insertUser(user.toUserLocalModel())
    }

    override suspend fun getUser(): UserDataModel {
        val user = userDao.getUser()
        user?.let {
            val accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
            val refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
            val ttl = sharedPreferences.getLong(TTL_KEY, 0)
            return it.toUserDataModel(accessToken, refreshToken, ttl)
        } ?: throw EsmorgaException(
            message = "User not found",
            source = Source.LOCAL,
            code = ErrorCodes.NOT_LOGGED_IN
        )
    }

    override suspend fun deleteUser() {
        sharedPreferences.edit().run {
            remove(ACCESS_TOKEN_KEY)
            remove(REFRESH_TOKEN_KEY)
            remove(TTL_KEY)
        }.apply()
        userDao.deleteUser()
    }
}
