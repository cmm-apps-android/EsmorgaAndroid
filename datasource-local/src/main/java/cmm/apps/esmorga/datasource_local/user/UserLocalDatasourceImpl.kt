package cmm.apps.esmorga.datasource_local.user

import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.datasource_local.database.dao.UserDao
import cmm.apps.esmorga.datasource_local.user.mapper.toUserDataModel
import cmm.apps.esmorga.datasource_local.user.mapper.toUserLocalModel

class UserLocalDatasourceImpl(private val userDao: UserDao):  UserDatasource {
    override suspend fun saveUser(user: UserDataModel) {
        userDao.insertUser(user.toUserLocalModel())
    }

    override suspend fun getUser(): UserDataModel {
        return userDao.getUser().toUserDataModel()
    }
}