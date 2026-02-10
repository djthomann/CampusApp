package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Canteen
import kotlinx.coroutines.flow.Flow

interface ICanteenService {

    fun getAllCanteens(): Flow<List<Canteen>>

    suspend fun getCanteenByName(name: String): Canteen?

}