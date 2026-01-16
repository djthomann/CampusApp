package hsrm.mi.campusapp.domain.model

import kotlinx.datetime.DayOfWeek

data class ScheduleEntry(
    val course: Course,
    val dayOfWeek: DayOfWeek
)
