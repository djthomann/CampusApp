package hsrm.mi.campusapp.data.persistence.canteen

import androidx.room.Entity
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.Canteen
import kotlinx.serialization.Serializable
import org.maplibre.spatialk.geojson.Position

@Entity
@Serializable
data class CanteenEntity(
    @PrimaryKey val name: String,
    val campus: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val url: String,
)

fun CanteenEntity.toDomain(): Canteen {
    return Canteen(
        name = name,
        campus = campus,
        position = Position(longitude, latitude),
        url = url,
    )
}