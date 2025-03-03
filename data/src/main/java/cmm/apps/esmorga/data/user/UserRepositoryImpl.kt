package cmm.apps.esmorga.data.user

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.mapper.toUser
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository

class UserRepositoryImpl(
    private val localUserDs: UserDatasource,
    private val remoteUserDs: UserDatasource,
    private val localEventDs: EventDatasource
) : UserRepository {

    override suspend fun login(email: String, password: String): User {
        return try {
            val userDataModel = remoteUserDs.login(email, password)
            localUserDs.saveUser(userDataModel)
            localEventDs.deleteCacheEvents()
            userDataModel.toUser()
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión: ${e.message}", e)
        }
    }

    override suspend fun register(name: String, lastName: String, email: String, password: String): User {
        return try {
            val userDataModel = remoteUserDs.register(name, lastName, email, password)
            localUserDs.saveUser(userDataModel)
            userDataModel.toUser()
        } catch (e: Exception) {
            throw Exception("Error al registrar usuario: ${e.message}", e)
        }
    }

    override suspend fun getUser(): User {
        return try {
            val userDataModel = localUserDs.getUser()
                ?: throw Exception("No se encontró un usuario almacenado localmente")
            userDataModel.toUser()
        } catch (e: Exception) {
            throw Exception("Error al obtener usuario: ${e.message}", e)
        }
    }

    override suspend fun logout() {
        try {
            localUserDs.deleteUser() // Elimina los datos del usuario almacenados localmente
            localEventDs.deleteCacheEvents() // Borra los eventos en caché
        } catch (e: Exception) {
            throw Exception("Error al cerrar sesión: ${e.message}", e)
        }
    }
}
