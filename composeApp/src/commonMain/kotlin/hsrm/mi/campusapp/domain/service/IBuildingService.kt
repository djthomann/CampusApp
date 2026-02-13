package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.building.BuildingEntity
import hsrm.mi.campusapp.domain.model.Building
import kotlinx.coroutines.flow.Flow

interface IBuildingService {

    fun getAllBuildings(): Flow<List<Building>>

    suspend fun getBuildingById(id: String): Building?

    suspend fun delete(entity: BuildingEntity)

    suspend fun deleteAll()

}