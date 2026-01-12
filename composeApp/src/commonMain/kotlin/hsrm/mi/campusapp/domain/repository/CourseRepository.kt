package hsrm.mi.campusapp.domain.repository

import hsrm.mi.campusapp.domain.model.Course
import kotlinx.datetime.LocalTime

object CourseRepository {

    val courses = listOf(
        Course("Human Computer Interaction", LocalTime(13, 15), LocalTime(14, 45)),
        Course("Algorithmen und Datenstrukturen", LocalTime(13, 15), LocalTime(14, 45)),
        Course("Angewandte Mathematik", LocalTime(13, 15), LocalTime(14, 45))
    )

}