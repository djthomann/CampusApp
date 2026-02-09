package hsrm.mi.campusapp.data.persistence

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import hsrm.mi.campusapp.data.persistence.campus.CampusDao
import hsrm.mi.campusapp.data.persistence.campus.toDomain
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.toEntity
import kotlinx.coroutines.test.runTest
import org.maplibre.spatialk.geojson.Position
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CampusDaoTests {

    private lateinit var db: AppDatabase
    private lateinit var dao: CampusDao

    @BeforeTest
    fun setup() {
        val builder = createTestDbBuilder()
        db = builder
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = db.getCampusDao()
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun testCreateAndRead() = runTest {

        dao.deleteAll()

        val data = Campus(
            name = "Test Campus",
            center = Position(1.0, 2.0),
            tilt = 10.0,
            jsonPath = "Test Path"
        )

        val countBefore = dao.count()
        dao.insert(data.toEntity())
        val countAfter = dao.count()

        assertEquals(countBefore, countAfter - 1)

        val extracted = dao.getByName("Test Campus")

        assertNotNull(extracted)
        assertEquals(data, extracted.toDomain())

    }

    @Test
    fun testUpdate() = runTest {

        dao.deleteAll()

        val model =  Campus(
            name = "Test Campus",
            center = Position(1.0, 2.0),
            tilt = 10.0,
            jsonPath = "Test Path"
        )
        dao.insert(model.toEntity())

        val updatedModel =  Campus(
            name = "Test Campus",
            center = Position(3.0, 4.0),
            tilt = 15.0,
            jsonPath = "Test Path Updated"
        )
        dao.insert(updatedModel.toEntity())

        val extractedEntity = dao.getByName("Test Campus")
        assertNotNull(extractedEntity)

        val extractedModel = extractedEntity.toDomain()
        assertEquals(updatedModel, extractedModel)

    }

    @Test
    fun testDelete() = runTest {

        dao.deleteAll()

        val data = Campus(
            name = "Test Campus",
            center = Position(1.0, 2.0),
            tilt = 10.0,
            jsonPath = "Test Path"
        )
        dao.insert(data.toEntity())

        assertEquals(1, dao.count())

        dao.delete(data.toEntity())

        val extractedEntity = dao.getByName("Test Campus")
        assertNull(extractedEntity)

    }

}