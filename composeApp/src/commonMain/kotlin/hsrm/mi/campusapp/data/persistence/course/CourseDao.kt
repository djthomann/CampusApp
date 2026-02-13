package hsrm.mi.campusapp.data.persistence.course

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CourseEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CourseEntity>)

    @Query("SELECT * FROM CourseEntity WHERE id = :id")
    suspend fun getById(id: Long): CourseEntity?

    @Transaction
    @Query("SELECT * FROM CourseEntity WHERE id = :id")
    suspend fun getCourseWithBuilding(id: Long): CourseWithBuilding?

    @Query("SELECT * FROM CourseEntity")
    fun getAllAsFlow(): Flow<List<CourseEntity>>

    @Transaction
    @Query("SELECT * FROM CourseEntity")
    fun getAllWithBuildingAsFlow(): Flow<List<CourseWithBuilding>>

    @Delete
    suspend fun delete(entity: CourseEntity)

    @Query("DELETE FROM CourseEntity WHERE id = :id")
    suspend fun deleteById(id: Long)


    @Query("DELETE FROM CourseEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM CourseEntity")
    suspend fun count(): Int

}