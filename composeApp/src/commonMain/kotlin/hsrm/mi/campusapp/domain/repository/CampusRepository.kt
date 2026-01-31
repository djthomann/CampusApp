package hsrm.mi.campusapp.domain.repository

import hsrm.mi.campusapp.domain.model.Campus
import org.maplibre.spatialk.geojson.Position

object CampusRepository {

    val campuses = listOf<Campus>(
        Campus("Kurt-Schumacher-Ring", Position( 8.217, 50.08), 0.0, "ksr.geojson"),
        Campus("Unter den Eichen", Position( 8.217, 50.0964), 0.0, "ude.geojson"),
        Campus("Rüsselsheim", Position( 8.424, 49.985), 0.0, "ruesselsheim.geojson"),
    )

    fun getCampusByName(name: String): Campus? {
        return campuses.find { it.name.equals(name, ignoreCase = true) }
    }

}