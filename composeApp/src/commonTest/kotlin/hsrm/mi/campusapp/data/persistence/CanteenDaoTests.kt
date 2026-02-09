package hsrm.mi.campusapp.data.persistence

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.toEntity
import hsrm.mi.campusapp.domain.persistence.AppDatabase
import hsrm.mi.campusapp.domain.persistence.canteen.CanteenDao
import hsrm.mi.campusapp.domain.persistence.canteen.toDomain
import kotlinx.coroutines.test.runTest
import org.maplibre.spatialk.geojson.Position
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull


class CanteenDaoTests {

    private lateinit var db: AppDatabase
    private lateinit var dao: CanteenDao

    @BeforeTest
    fun setup() {
        val builder = createTestDbBuilder()
        db = builder
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = db.getCanteenDao()
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun testCreateAndRead() = runTest {

        dao.deleteAll()

        val data = Canteen(
            name = "Test Canteen",
            campus = "Test Campus",
            position = Position(1.0, 1.0),
            url = "Test URL"
        )

        val countBefore = dao.count()
        dao.insert(data.toEntity())
        val countAfter = dao.count()

        assertEquals(countBefore, countAfter - 1)

        val extracted = dao.getByName("Test Canteen")

        assertNotNull(extracted)
        assertEquals(data, extracted.toDomain())

    }

    @Test
    fun testUpdate() = runTest {

        dao.deleteAll()

        val model = Canteen(
            name = "Test Canteen",
            campus = "Test Campus",
            position = Position(1.0, 1.0),
            url = "Test URL"
        )
        dao.insert(model.toEntity())

        val updatedModel = Canteen(
            name = "Test Canteen",
            campus = "Test Campus Updated",
            position = Position(2.0, 3.0),
            url = "Test URL Updated"
        )
        dao.insert(updatedModel.toEntity())

        val extractedEntity = dao.getByName("Test Canteen")
        assertNotNull(extractedEntity)

        val extractedModel = extractedEntity.toDomain()
        assertEquals(updatedModel, extractedModel)

    }

    @Test
    fun testDelete() = runTest {

        dao.deleteAll()

        val data = Canteen(
            name = "Test Canteen",
            campus = "Test Campus",
            position = Position(1.0, 1.0),
            url = "Test URL"
        )
        dao.insert(data.toEntity())

        assertEquals(1, dao.count())

        dao.delete(data.toEntity())

        val extractedEntity = dao.getByName("Test Canteen")
        assertNull(extractedEntity)

    }

}