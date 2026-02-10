package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.canteen.CanteenDao
import hsrm.mi.campusapp.data.persistence.canteen.toDomain
import hsrm.mi.campusapp.domain.model.Canteen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CanteenService(
    private val dao: CanteenDao
): ICanteenService {

    override fun getAllCanteens(): Flow<List<Canteen>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getCanteenByName(name: String): Canteen? {
        return dao.getByName(name)?.toDomain()
    }

}