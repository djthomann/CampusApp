package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.campus.CampusDao
import hsrm.mi.campusapp.data.persistence.campus.CampusEntity
import hsrm.mi.campusapp.data.persistence.campus.toDomain
import hsrm.mi.campusapp.domain.model.Campus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CampusService (
    private val dao: CampusDao
): ICampusService {

    override fun getAllCampuses(): Flow<List<Campus>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getCampusByName(name: String): Campus? {
        return dao.getByName(name)?.toDomain()
    }

    override suspend fun delete(entity: CampusEntity) {
        dao.delete(entity)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

}