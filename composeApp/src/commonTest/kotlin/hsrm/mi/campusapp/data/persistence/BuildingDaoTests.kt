package hsrm.mi.campusapp.data.persistence

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import hsrm.mi.campusapp.data.persistence.building.BuildingDao
import hsrm.mi.campusapp.data.persistence.building.toDomain
import hsrm.mi.campusapp.data.persistence.canteen.CanteenDao
import hsrm.mi.campusapp.data.persistence.canteen.toDomain
import hsrm.mi.campusapp.domain.model.Building
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.toEntity
import kotlinx.coroutines.test.runTest
import org.maplibre.spatialk.geojson.Position
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BuildingDaoTests {

    private lateinit var db: AppDatabase
    private lateinit var dao: BuildingDao

    @BeforeTest
    fun setup() {
        val builder = createTestDbBuilder()
        db = builder
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = db.getBuildingDao()
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun testCreateAndRead() = runTest {

        dao.deleteAll()

        val data = Building(
            id = "Test ID",
            name = "Test Building",
            longitude = 1.0,
            latitude = 20.0
        )

        val countBefore = dao.count()
        dao.insert(data.toEntity())
        val countAfter = dao.count()

        assertEquals(countBefore, countAfter - 1)

        val extracted = dao.getById("Test ID")

        assertNotNull(extracted)
        assertEquals(data, extracted.toDomain())

    }

    @Test
    fun testUpdate() = runTest {

        dao.deleteAll()

        val model = Building(
            id = "Test ID",
            name = "Test Building",
            longitude = 1.0,
            latitude = 20.0
        )
        dao.insert(model.toEntity())

        val updatedModel = Building(
            id = "Test ID",
            name = "Test Building Updated",
            longitude = 2.0,
            latitude = 21.0
        )
        dao.insert(updatedModel.toEntity())

        val extractedEntity = dao.getById("Test ID")
        assertNotNull(extractedEntity)

        val extractedModel = extractedEntity.toDomain()
        assertEquals(updatedModel, extractedModel)

    }

    @Test
    fun testDelete() = runTest {

        dao.deleteAll()

        val data = Building(
            id = "Test ID",
            name = "Test Building Updated",
            longitude = 2.0,
            latitude = 21.0
        )
        dao.insert(data.toEntity())

        assertEquals(1, dao.count())

        dao.delete(data.toEntity())

        val extractedEntity = dao.getById("Test ID")
        assertNull(extractedEntity)

    }

}