package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Stop
import kotlinx.coroutines.flow.Flow

class TestStopService: IStopService {
    override suspend fun saveStop(stop: Stop) {
        TODO("Not yet implemented")
    }

    override fun getAllStops(): Flow<List<Stop>> {
        TODO("Not yet implemented")
    }

    override fun getStopsForCampusName(name: String): Flow<List<Stop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getStopById(name: String): Stop? {
        TODO("Not yet implemented")
    }
}