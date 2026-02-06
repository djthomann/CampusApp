package hsrm.mi.campusapp.domain.persistence

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import campusapp.composeapp.generated.resources.Res
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
        seedStopsFromResource(db.getStopDao())
        seedCanteensFromResource(db.getCanteenDao())
    }

    return db
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

@Database(entities = [
    DishEntity::class,
    MenuEntity::class,
    SideDishEntity::class,
    StopEntity::class,
    CanteenEntity::class
                     ], version = 15)
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
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
