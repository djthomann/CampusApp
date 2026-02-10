package hsrm.mi.campusapp.presentation

import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.service.ICampusService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.maplibre.spatialk.geojson.Position

class TestCampusService(): ICampusService {

    private val mockCampuses = listOf(
        Campus(
            name = "Campus 1",
            center = Position(1.0, 2.0),
            tilt = 25.0,
            jsonPath = "Test Path"
        )
    )

    override fun getAllCampuses(): Flow<List<Campus>> = flow {
        emit(mockCampuses)
    }

    override suspend fun getCampusByName(name: String): Campus? {
        return mockCampuses.find { it.name == name }
    }
}