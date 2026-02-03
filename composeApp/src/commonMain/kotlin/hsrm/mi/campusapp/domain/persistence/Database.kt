package hsrm.mi.campusapp.domain.persistence

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@Database(entities = [
    DishEntity::class,
    MenuEntity::class,
    SideDishEntity::class
                     ], version = 12)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(
    SideDishTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDishDao(): DishDao
    abstract fun getMenuDao(): MenuDao

    abstract fun getSideDishDao(): SideDishDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
