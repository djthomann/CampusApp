package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.canteen.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CanteenService {

    val dao = DatabaseHolder.db.getCanteenDao()

    fun getAllCanteens(): Flow<List<Canteen>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    suspend fun getCanteenByName(name: String): Canteen? {
        return dao.getByName(name)?.toDomain()
    }

}