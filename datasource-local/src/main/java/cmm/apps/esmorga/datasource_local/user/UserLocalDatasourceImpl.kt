package cmm.apps.esmorga.datasource_local.user

import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.database.dao.UserDao
import cmm.apps.esmorga.datasource_local.user.mapper.toUserDataModel
import cmm.apps.esmorga.datasource_local.user.mapper.toUserLocalModel
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source

class UserLocalDatasourceImpl(private val userDao: UserDao):  UserDatasource {
    override suspend fun saveUser(user: UserDataModel) {
        userDao.insertUser(user.toUserLocalModel())
    }

    override suspend fun getUser(): UserDataModel {
        val user = userDao.getUser()
        user?.let{
            return it.toUserDataModel()
        } ?: throw EsmorgaException(message = "User not found", source = Source.LOCAL, code = ErrorCodes.NO_DATA)
    }
}