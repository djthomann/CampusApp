package hsrm.mi.campusapp.data.persistence

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import campusapp.composeapp.generated.resources.Res
import hsrm.mi.campusapp.data.persistence.building.BuildingDao
import hsrm.mi.campusapp.data.persistence.building.BuildingEntity
import hsrm.mi.campusapp.data.persistence.campus.CampusDao
import hsrm.mi.campusapp.data.persistence.campus.CampusEntity
import hsrm.mi.campusapp.data.persistence.canteen.CanteenDao
import hsrm.mi.campusapp.data.persistence.canteen.CanteenEntity
import hsrm.mi.campusapp.data.persistence.course.CourseDao
import hsrm.mi.campusapp.data.persistence.course.CourseEntity
import hsrm.mi.campusapp.data.persistence.menu.DishDao
import hsrm.mi.campusapp.data.persistence.menu.DishEntity
import hsrm.mi.campusapp.data.persistence.menu.MenuDao
import hsrm.mi.campusapp.data.persistence.menu.MenuEntity
import hsrm.mi.campusapp.data.persistence.menu.SideDishDao
import hsrm.mi.campusapp.data.persistence.menu.SideDishEntity
import hsrm.mi.campusapp.data.persistence.menu.SideDishTypeConverter
import hsrm.mi.campusapp.data.persistence.stop.StopDao
import hsrm.mi.campusapp.data.persistence.stop.StopEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>,
    scope: CoroutineScope
): AppDatabase {
    val db = builder
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

    // Seed Database
    scope.launch {
        seedBuildingsFromResource(db.getBuildingDao())
        seedCampusFromResource(db.getCampusDao())
        seedStopsFromResource(db.getStopDao())
        seedCanteensFromResource(db.getCanteenDao())
        seedCoursesFromResource(db.getCourseDao())
    }

    return db
}

suspend fun seedCoursesFromResource(dao: CourseDao) {

    if (dao.count() == 0) {
        println("SEEDING DB WITH COURSES...")
        try {
            val jsonString = Res.readBytes("files/model/courses.json").decodeToString()
            val items = Json.decodeFromString<List<CourseEntity>>(jsonString)
            dao.insertAll(items)
            println("SEEDING COURSES FINISHED")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    println("LOADED COURSES: ${dao.count()}")
}

suspend fun seedStopsFromResource(dao: StopDao) {

    if (dao.count() == 0) {
        println("SEEDING DB WITH STOPS...")
        try {
            val jsonString = Res.readBytes("files/model/stops.json").decodeToString()
            val items = Json.decodeFromString<List<StopEntity>>(jsonString)
            dao.insertAll(items)
            println("SEEDING STOPS FINISHED")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    println("LOADED STOPS: ${dao.count()}")
}

suspend fun seedCampusFromResource(dao: CampusDao) {

    if (dao.count() == 0) {
        println("SEEDING DB WITH CAMPUSES...")
        try {
            val jsonString = Res.readBytes("files/model/campuses.json").decodeToString()
            val items = Json.decodeFromString<List<CampusEntity>>(jsonString)
            dao.insertAll(items)
            println("SEEDING CAMPUSES FINISHED")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    println("LOADED CAMPUSES: ${dao.count()}")

}

suspend fun seedCanteensFromResource(dao: CanteenDao) {

    if (dao.count() == 0) {
        println("SEEDING DB WITH CANTEENS...")
        try {
            val jsonString = Res.readBytes("files/model/canteens.json").decodeToString()
            val items = Json.decodeFromString<List<CanteenEntity>>(jsonString)
            dao.insertAll(items)
            println("SEEDING CANTEENS FINISHED")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    println("LOADED CANTEENS: ${dao.count()}")
}

suspend fun seedBuildingsFromResource(dao: BuildingDao) {

    if (dao.count() == 0) {
        println("SEEDING DB WITH BUILDINGS...")
        try {
            val jsonString = Res.readBytes("files/model/buildings.json").decodeToString()
            val items = Json.decodeFromString<List<BuildingEntity>>(jsonString)
            dao.insertAll(items)
            println("SEEDING BUILDINGS FINISHED")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    println("LOADED BUILDINGS: ${dao.count()}")
}

@Database(entities = [
    CampusEntity::class,
    DishEntity::class,
    MenuEntity::class,
    SideDishEntity::class,
    StopEntity::class,
    CanteenEntity::class,
    CourseEntity::class,
    BuildingEntity::class
                     ], version = 35)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(
    SideDishTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDishDao(): DishDao
    abstract fun getMenuDao(): MenuDao
    abstract fun getSideDishDao(): SideDishDao
    abstract fun getStopDao(): StopDao
    abstract fun getCanteenDao(): CanteenDao
    abstract fun getCampusDao(): CampusDao
    abstract fun getCourseDao(): CourseDao

    abstract fun getBuildingDao(): BuildingDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
