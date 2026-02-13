package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.building.BuildingDao
import hsrm.mi.campusapp.data.persistence.building.BuildingEntity
import hsrm.mi.campusapp.data.persistence.building.toDomain
import hsrm.mi.campusapp.domain.model.Building
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BuildingService(
    private val dao: BuildingDao
): IBuildingService {

    override fun getAllBuildings(): Flow<List<Building>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getBuildingById(id: String): Building? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun delete(entity: BuildingEntity) {
        dao.delete(entity)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}