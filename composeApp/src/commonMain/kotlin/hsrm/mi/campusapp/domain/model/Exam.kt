package hsrm.mi.campusapp.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class Exam(
    val name: String,
    val studentEnrolled: Boolean = false,
    val date: LocalDate,
    val time: LocalTime,
    val building: Building,
    val room: String,
)