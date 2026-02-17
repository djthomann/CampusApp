package hsrm.mi.campusapp.data.persistence.exam

import androidx.room.Embedded
import androidx.room.Relation
import hsrm.mi.campusapp.data.persistence.building.BuildingEntity
import hsrm.mi.campusapp.data.persistence.building.toDomain
import hsrm.mi.campusapp.domain.model.Exam
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ExamWithBuilding (
    @Embedded val exam: ExamEntity,
    @Relation(
        parentColumn = "buildingId",
        entityColumn = "id"
    )
    val building: BuildingEntity
)

fun ExamWithBuilding.toDomain(): Exam {
    return Exam(
        name = exam.name,
        studentEnrolled = exam.studentEnrolled,
        date = LocalDate.parse(exam.date),
        time = LocalTime.parse(exam.time),
        building = building.toDomain(),
        room = exam.room
    )
}