package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Stop
import kotlinx.coroutines.flow.Flow

interface IStopService {

    suspend fun saveStop(stop: Stop)
    fun getAllStops(): Flow<List<Stop>>
    fun getStopsForCampusName(name: String): Flow<List<Stop>>
    suspend fun getStopById(name: String): Stop?

}