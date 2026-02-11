package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.campus.CampusEntity
import hsrm.mi.campusapp.domain.model.Campus
import kotlinx.coroutines.flow.Flow

// TODO(): ICampusService should have same functions as CampusDao
interface ICampusService {

    fun getAllCampuses(): Flow<List<Campus>>

    suspend fun getCampusByName(name: String): Campus?

    suspend fun delete(entity: CampusEntity)

    suspend fun deleteAll()


}