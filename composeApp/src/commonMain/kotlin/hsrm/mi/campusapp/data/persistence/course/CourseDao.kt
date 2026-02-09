package hsrm.mi.campusapp.data.persistence.course

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CourseEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CourseEntity>)

    @Query("SELECT * FROM CourseEntity WHERE name = :name")
    suspend fun getByName(name: String): CourseEntity?

    @Query("SELECT * FROM CourseEntity")
    fun getAllAsFlow(): Flow<List<CourseEntity>>

    @Delete
    suspend fun delete(dish: CourseEntity)

    @Query("DELETE FROM CourseEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM CourseEntity")
    suspend fun count(): Int

}