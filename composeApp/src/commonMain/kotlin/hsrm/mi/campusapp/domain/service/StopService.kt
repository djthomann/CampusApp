package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.stop.StopDao
import hsrm.mi.campusapp.data.persistence.stop.toDomain
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StopService(
    private val dao: StopDao
): IStopService {

    override suspend fun saveStop(stop: Stop) {
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

    override fun getAllStops(): Flow<List<Stop>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }
    override fun getStopsForCampusName(name: String): Flow<List<Stop>> {
        return getAllStops().map { stops -> stops.filter { stop -> stop.campus == name } }
    }

    override suspend fun getStopById(name: String): Stop? {
        return dao.getById(name)?.toDomain()
    }

}