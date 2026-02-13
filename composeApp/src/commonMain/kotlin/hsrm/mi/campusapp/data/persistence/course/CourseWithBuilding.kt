package hsrm.mi.campusapp.data.persistence.course

import androidx.room.Embedded
import androidx.room.Relation
import hsrm.mi.campusapp.data.persistence.building.BuildingEntity
import hsrm.mi.campusapp.data.persistence.building.toDomain
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.CourseType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

data class CourseWithBuilding(
    @Embedded val course: CourseEntity,
    @Relation(
        parentColumn = "buildingId",
        entityColumn = "id"
    )
    val building: BuildingEntity
)

fun CourseWithBuilding.toDomain(): Course {
    return Course(
        name = course.name,
        dayOfWeek = DayOfWeek.valueOf(course.dayOfWeek),
        start = LocalTime.parse(course.start),
        durationInMinutes = course.durationInMinutes,
        lecturer = course.lecturer,
        room = course.room,
        courseType = CourseType.valueOf(course.courseType),
        building = building.toDomain()
    )
}