package hsrm.mi.campusapp.domain.model

import hsrm.mi.campusapp.domain.persistence.campus.CampusEntity
import hsrm.mi.campusapp.domain.persistence.canteen.CanteenEntity
import hsrm.mi.campusapp.domain.persistence.course.CourseEntity
import hsrm.mi.campusapp.domain.persistence.menu.MenuEntity
import hsrm.mi.campusapp.domain.persistence.stop.StopEntity
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.maplibre.spatialk.geojson.Position
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelToEntityTests {

    @Test
    fun testStopToEntity() {

        val expected = StopEntity(
            id = "Test ID",
            name = "Test Stop",
            campus = "Test Campus",
            longitude = 1.0,
            latitude = 2.0
        )

        val model = Stop(
            id = "Test ID",
            name = "Test Stop",
            campus = "Test Campus",
            position = Position(1.0, 2.0)
        )

        val actual = model.toEntity()

        assertEquals(expected, actual)

    }

    @Test
    fun testCampusToEntity() {

        val expected = CampusEntity(
            name = "Test Campus",
            longitude = 10.0,
            latitude = 20.0,
            tilt = 1.0,
            jsonPath = "Test Path"
        )

        val model = Campus(
            name = "Test Campus",
            center = Position(10.0, 20.0),
            tilt = 1.0,
            jsonPath = "Test Path"
        )

        val actual = model.toEntity()

        assertEquals(expected, actual)

    }

    @Test
    fun testCanteenToEntity() {

        val expected = CanteenEntity(
            name = "Test Canteen",
            campus = "Test Campus",
            longitude = 90.0,
            latitude = 35.0,
            url = "/test-canteen"
        )

        val model = Canteen(
            name = "Test Canteen",
            campus = "Test Campus",
            position = Position(90.0, 35.0),
            url = "/test-canteen"
        )

        val actual = model.toEntity()

        assertEquals(expected, actual)

    }

    @Test
    fun testCourseToEntity() {

        val expected = CourseEntity(
            name = "Test Course",
            dayOfWeek = "MONDAY",
            start = "16:30",
            durationInMinutes = 90,
            lecturer = "Test Lecturer",
            room = "Room 101",
            courseType = "PRACTICAL",
            longitude = 12.0,
            latitude = 14.0
        )

        val model = Course(
            name = "Test Course",
            dayOfWeek = DayOfWeek.MONDAY,
            start = LocalTime(16, 30),
            durationInMinutes = 90,
            lecturer = "Test Lecturer",
            room = "Room 101",
            courseType = CourseType.PRACTICAL,
            building = Position(12.0, 14.0)
        )

        val actual = model.toEntity()

        assertEquals(expected, actual)

    }

    @Test
    fun testMenuToEntity() {

        val expected = MenuEntity(
            canteen = "Test Canteen",
            date = "2026-01-01",
            dateString = "January 1st"
        )

        val model = Menu(
            canteen = "Test Canteen",
            date = LocalDate(2026, 1, 1),
            dateString = "January 1st",
            dishes = emptyList(),
            sideDishes = emptyMap()
        )

        val actual = model.toEntity()

        assertEquals(expected, actual)

    }

}