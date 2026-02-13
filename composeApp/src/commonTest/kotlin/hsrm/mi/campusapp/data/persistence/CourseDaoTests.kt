package hsrm.mi.campusapp.data.persistence

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import hsrm.mi.campusapp.data.persistence.building.BuildingDao
import hsrm.mi.campusapp.data.persistence.course.CourseDao
import hsrm.mi.campusapp.data.persistence.course.CourseEntity
import hsrm.mi.campusapp.data.persistence.course.toDomain
import hsrm.mi.campusapp.domain.model.Building
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.CourseType
import hsrm.mi.campusapp.domain.model.toEntity
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CourseDaoTests {

    private lateinit var db: AppDatabase
    private lateinit var dao: CourseDao

    private lateinit var buildingDao: BuildingDao

    @BeforeTest
    fun setup() {
        val builder = createTestDbBuilder()
        db = builder
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = db.getCourseDao()
        buildingDao = db.getBuildingDao()
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun testCreateAndRead() = runTest {

        dao.deleteAll()
        buildingDao.deleteAll()

        val testBuilding = Building(
            id = "Test ID",
            name = "Test Building",
            longitude = 1.0,
            latitude = 2.0
        )
        buildingDao.insert(testBuilding.toEntity())

        val data = Course(
            name = "Test Course",
            dayOfWeek = DayOfWeek.TUESDAY,
            start = LocalTime(10, 0),
            durationInMinutes = 90,
            lecturer = "Test Lecturer",
            room = "D11",
            courseType = CourseType.LECTURE,
            building = testBuilding
        )

        val countBefore = dao.count()
        val entityId = dao.insert(data.toEntity())
        val countAfter = dao.count()

        assertEquals(countBefore, countAfter - 1)

        val extracted = dao.getCourseWithBuilding(entityId)

        assertNotNull(extracted)
        assertEquals(data, extracted.toDomain())

    }

    @Test
    fun testUpdate() = runTest {

        dao.deleteAll()
        buildingDao.deleteAll()

        val testBuilding = Building(
            id = "Test ID",
            name = "Test Building",
            longitude = 1.0,
            latitude = 2.0
        )
        buildingDao.insert(testBuilding.toEntity())

        val model = Course(
            name = "Test Course",
            dayOfWeek = DayOfWeek.TUESDAY,
            start = LocalTime(10, 0),
            durationInMinutes = 90,
            lecturer = "Test Lecturer",
            room = "D11",
            courseType = CourseType.LECTURE,
            building = testBuilding
        )
        dao.insert(model.toEntity())

        val newBuilding = Building(
            id = "Test ID",
            name = "Test Building 2",
            longitude = 2.0,
            latitude = 3.0
        )
        buildingDao.insert(newBuilding.toEntity())

        val updatedModel = Course(
            name = "Test Course",
            dayOfWeek = DayOfWeek.WEDNESDAY,
            start = LocalTime(11, 5),
            durationInMinutes = 100,
            lecturer = "Test Lecturer Updated",
            room = "D12",
            courseType = CourseType.PRACTICAL,
            building = newBuilding
        )
        val entityId = dao.insert(updatedModel.toEntity())

        val extractedEntity = dao.getCourseWithBuilding(entityId)
        assertNotNull(extractedEntity)

        val extractedModel = extractedEntity.toDomain()
        assertEquals(updatedModel, extractedModel)

    }

    @Test
    fun testDelete() = runTest {

        dao.deleteAll()
        buildingDao.deleteAll()

        val testBuilding = Building(
            id = "Test ID",
            name = "Test Building",
            longitude = 1.0,
            latitude = 2.0
        )
        buildingDao.insert(testBuilding.toEntity())

        val data = Course(
            name = "Test Course",
            dayOfWeek = DayOfWeek.TUESDAY,
            start = LocalTime(10, 0),
            durationInMinutes = 90,
            lecturer = "Test Lecturer",
            room = "D11",
            courseType = CourseType.LECTURE,
            building = testBuilding
        )
        val entityId = dao.insert(data.toEntity())

        assertEquals(1, dao.count())

        dao.deleteById(entityId)

        val extractedEntity = dao.getById(entityId)
        assertNull(extractedEntity)

    }

}