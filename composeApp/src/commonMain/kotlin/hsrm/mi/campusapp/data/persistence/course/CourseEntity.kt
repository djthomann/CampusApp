package hsrm.mi.campusapp.data.persistence.course

import androidx.room.Entity
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.CourseType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import org.maplibre.spatialk.geojson.Position

@Entity
@Serializable
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dayOfWeek: String,
    val start: String,
    val durationInMinutes: Int,
    val lecturer: String? = null,
    val room: String,
    val courseType: String,
    val longitude: Double,
    val latitude: Double
)

fun CourseEntity.toDomain(): Course {
    return Course(
        name = name,
        dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
        start = LocalTime.parse(start),
        durationInMinutes = durationInMinutes,
        lecturer = lecturer,
        room = room,
        courseType = CourseType.valueOf(courseType),
        building = Position(longitude, latitude)
    )
}