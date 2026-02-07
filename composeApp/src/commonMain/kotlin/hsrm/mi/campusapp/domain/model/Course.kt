package hsrm.mi.campusapp.domain.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import org.maplibre.spatialk.geojson.Position

data class Course(
    val name: String,
    val dayOfWeek: DayOfWeek,
    val start: LocalTime,
    val durationInMinutes: Int,
    val lecturer: String? = null,
    val room: String,
    val courseType: CourseType,
    val building: Position
)

