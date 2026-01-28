package hsrm.mi.campusapp.domain.repository

import hsrm.mi.campusapp.domain.model.Canteen
import org.maplibre.spatialk.geojson.Position

object CanteenRepository {

    val canteens = listOf<Canteen>(
        Canteen("Mensa accent","Kurt-Schumacher-Ring", Position( 8.218216, 50.081457)),
        Canteen("Mensa Rüsselsheim","Rüsselsheim",  Position( 8.218216, 50.081457)),
        Canteen("Mensa POINT","Bleichstraße",  Position( 8.218216, 50.081457))
    )

}