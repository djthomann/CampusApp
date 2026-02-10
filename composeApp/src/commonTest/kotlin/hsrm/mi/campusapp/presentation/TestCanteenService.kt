package hsrm.mi.campusapp.presentation

import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.service.ICanteenService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.maplibre.spatialk.geojson.Position

class TestCanteenService: ICanteenService {

    private val mockCanteens = listOf(
        Canteen(
            name = "Mensa 1",
            campus = "Campus 1",
            position = Position(1.0, 2.0),
            url = "URL 1"
        ),
        // Canteen(id = 2, name = "Mensa Unter den Eichen", city = "Wiesbaden"),
        // Canteen(id = 3, name = "Mensa Bertramstraße", city = "Wiesbaden")
    )

    override fun getAllCanteens(): Flow<List<Canteen>> = flow {
        emit(mockCanteens)
    }

    override suspend fun getCanteenByName(name: String): Canteen? {
        return mockCanteens.find { it.name == name }
    }

}
