package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Campus
import kotlinx.coroutines.flow.Flow

interface ICampusService {

    fun getAllCampuses(): Flow<List<Campus>>

    suspend fun getCampusByName(name: String): Campus?

}