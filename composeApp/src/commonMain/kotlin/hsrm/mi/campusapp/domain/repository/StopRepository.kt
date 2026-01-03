package hsrm.mi.campusapp.domain.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import hsrm.mi.campusapp.domain.model.Stop
import io.github.dellisd.spatialk.geojson.Position

object StopRepository {
    val stops = listOf<Stop>(
        Stop("A=1@O=Wiesbaden Hochschule RheinMain@X=8218604@Y=50081135@U=80@L=3018265@p=1767293043@","Kurt-Schumacher-Ring", "Hochschule RheinMain",  Position( 8.218216, 50.081457)),
        Stop("A=1@O=Wiesbaden Nordfriedhof@X=8221031@Y=50097028@U=80@L=3025457@p=1767293043@", "Unter den Eichen", "Nordfriedhof",  Position( 8.220445, 50.097103)),
        Stop("A=1@O=Wiesbaden Unter den Eichen@X=8219278@Y=50096281@U=80@L=3018170@p=1767293043@","Unter den Eichen", "Unter den Eichen",  Position( 8.219030, 50.096314)),
        Stop("A=1@O=Rüsselsheim Hochschule-Rhein-Main@X=8422371@Y=49985453@U=80@L=3014366@p=1767293043@", "Rüsselsheim", "Hochschule Rhein-Main", Position( 8.218216, 50.081457))
    )

    var selectedStop by mutableStateOf(stops.first())
        private set

    fun selectStop(stop: Stop) {
        println("Test")
        selectedStop = stop
    }

    fun getStopsForCampusName(name: String): List<Stop> {
        return stops.filter { it.campus == name }
    }
}