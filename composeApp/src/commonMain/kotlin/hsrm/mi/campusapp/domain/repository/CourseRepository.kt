package hsrm.mi.campusapp.domain.repository

import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.CourseType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

object CourseRepository {

    val courses = listOf(
        Course("Human Computer Interaction", DayOfWeek.MONDAY,LocalTime(14, 15), 90, lecturer = "Prof. Dr. Marion Koelle", room = "D11", courseType = CourseType.LECTURE),
        Course("Human Computer Interaction", DayOfWeek.MONDAY,LocalTime(16, 0), 90, lecturer = "Prof. Dr. Marion Koelle", room = "D12", courseType = CourseType.PRACTICAL),
        Course("Algorithmen und Datenstrukturen", DayOfWeek.WEDNESDAY, LocalTime(13, 15), 90, lecturer = "Prof. Dr. Dirk Krechel", room = "D14", courseType = CourseType.LECTURE),
        Course("Angewandte Mathematik", DayOfWeek.FRIDAY,LocalTime(13, 15), 90, lecturer = "Prof. Dr. Schwanecke", room = "D15", courseType = CourseType.PRACTICAL)
    )

    fun getCoursesForDayOfWeek(dayOfWeek: DayOfWeek): List<Course> {
        return courses.filter { course -> course.dayOfWeek == dayOfWeek }
    }

}