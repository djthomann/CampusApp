package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.DatabaseHolder
import hsrm.mi.campusapp.data.persistence.canteen.toDomain
import hsrm.mi.campusapp.domain.model.Canteen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CanteenService: ICanteenService {

    val dao = DatabaseHolder.db.getCanteenDao()

    fun getAllCanteens(): Flow<List<Canteen>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    suspend fun getCanteenByName(name: String): Canteen? {
        return dao.getByName(name)?.toDomain()
    }

}