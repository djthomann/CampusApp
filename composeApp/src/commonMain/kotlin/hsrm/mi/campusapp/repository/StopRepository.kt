package hsrm.mi.campusapp.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import hsrm.mi.campusapp.model.Stop
import io.github.dellisd.spatialk.geojson.Position

object StopRepository {
    val stops = listOf<Stop>(
        Stop("Kurt-Schumacher-Ring", "Hochschule Rhein-Main",  Position( 8.218216, 50.081457)),
        Stop("Unter den Eichen", "Nordfriedhof",  Position( 8.220445, 50.097103)),
        Stop("Unter den Eichen", "Unter den Eichen",  Position( 8.219030, 50.096314))
    )

    var selectedStop by mutableStateOf(stops.first())
        private set

    fun selectStop(stop: Stop) {
        selectedStop = stop
    }

    fun getStopsForCampusName(name: String): List<Stop> {
        return stops.filter { it.campus == name }
    }
}