package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.toDomain
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.domain.repository.CanteenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CampusService {

    val dao = DatabaseHolder.db.getCampusDao()

    fun getAllCampuses(): Flow<List<Campus>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    fun getCampusByName(name: String): Campus? {
        return dao.getByName(name)?.toDomain()
    }

}