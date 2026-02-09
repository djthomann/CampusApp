package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.model.toEntity
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.stop.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object StopService {

    val dao = DatabaseHolder.db.getStopDao()

    suspend fun saveStop(stop: Stop) {
        val entity = stop.toEntity()
        // println("Versuche ID zu speichern: ${entity.id}")

        try {
            dao.insert(entity)
        } catch (e: Exception) {
            println("Fehler beim Insert: ${e.message}")
        }

        val result = getStopById(stop.id)
        // println("DB STATUS: $result")
    }

    fun getAllStops(): Flow<List<Stop>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    // Could also be implemented in DAO
    fun getStopsForCampusName(name: String): Flow<List<Stop>> {
        return getAllStops().map { stops -> stops.filter { stop -> stop.campus == name } }
    }

    suspend fun getStopById(name: String): Stop? {
        return dao.getById(name)?.toDomain()
    }

}