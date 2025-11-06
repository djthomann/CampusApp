package hsrm.mi.campusapp.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import hsrm.mi.campusapp.model.Campus
import io.github.dellisd.spatialk.geojson.Position

object CampusRepository {

    val campuses = listOf<Campus>(
        Campus("Kurt-Schumacher-Ring", Position( 8.217, 50.08), 0.0),
        Campus("Unter den Eichen", Position( 8.217, 50.0964), 0.0),
        Campus("Campus Rüsselsheim", Position( 8.424, 49.985), 0.0),
    )

    var selectedCampus by mutableStateOf(campuses.first())
        private set

    fun selectCampus(campus: Campus) {
        println(  "Campus changed in repo: $campus")
        selectedCampus = campus
    }

}