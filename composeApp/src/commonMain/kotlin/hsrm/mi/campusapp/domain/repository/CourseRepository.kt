package hsrm.mi.campusapp.domain.repository

import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.CourseType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import org.maplibre.spatialk.geojson.Position

object CourseRepository {

    val courses = listOf(
        Course("Human Computer Interaction", DayOfWeek.MONDAY,LocalTime(14, 15), 90, lecturer = "Prof. Dr. Marion Koelle", room = "D11", courseType = CourseType.LECTURE,
            Position(8.21618070647615, 50.097631477365795)),
        Course("Human Computer Interaction", DayOfWeek.MONDAY,LocalTime(16, 0), 90, lecturer = "Prof. Dr. Marion Koelle", room = "D12", courseType = CourseType.PRACTICAL, Position(8.21618070647615, 50.097631477365795)),
        Course("Algorithmen und Datenstrukturen", DayOfWeek.WEDNESDAY, LocalTime(13, 15), 90, lecturer = "Prof. Dr. Dirk Krechel", room = "D14", courseType = CourseType.LECTURE, Position(8.216929495921237, 50.096888448950054)),
        Course("Angewandte Mathematik", DayOfWeek.FRIDAY,LocalTime(13, 15), 90, lecturer = "Prof. Dr. Schwanecke", room = "D15", courseType = CourseType.PRACTICAL,
            Position( 8.215954211691997, 50.09850095851942)),
        Course("Senatssitzung", DayOfWeek.TUESDAY,LocalTime(17, 15), 90, room = "A115", courseType = CourseType.EXTRA_CURRICULAR,
            building = Position( 8.217439687773457, 50.08093675702284))
    )

    fun getCoursesForDayOfWeek(dayOfWeek: DayOfWeek): List<Course> {
        return courses.filter { course -> course.dayOfWeek == dayOfWeek }
    }

}