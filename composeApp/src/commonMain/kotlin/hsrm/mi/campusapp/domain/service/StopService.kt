package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object StopService {

    val dao = DatabaseHolder.db.getStopDao()

    fun getAllStops(): Flow<List<Stop>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    // Could also be implemented in DAO
    fun getStopsForCampusName(name: String): Flow<List<Stop>> {
        return getAllStops().map { stops -> stops.filter { stop -> stop.campus == name } }
    }

    suspend fun getStopByName(name: String): Stop? {
        return dao.getByName(name)?.toDomain()
    }

}