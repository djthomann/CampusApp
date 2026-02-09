package hsrm.mi.campusapp.domain.model

import hsrm.mi.campusapp.data.persistence.course.CourseEntity
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

fun Course.toEntity(): CourseEntity {
    return CourseEntity(
        name = name,
        dayOfWeek = dayOfWeek.name,
        start = start.toString(),
        durationInMinutes = durationInMinutes,
        lecturer = lecturer,
        room = room,
        courseType = courseType.name,
        longitude = building.longitude,
        latitude = building.latitude
    )
}

