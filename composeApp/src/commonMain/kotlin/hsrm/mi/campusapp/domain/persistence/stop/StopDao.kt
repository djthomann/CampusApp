package hsrm.mi.campusapp.domain.persistence.stop

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: StopEntity)

    @Insert
    suspend fun insertAll(items: List<StopEntity>)

    @Query("SELECT * FROM StopEntity WHERE id = :id")
    suspend fun getById(id: String): StopEntity?
    @Query("SELECT * FROM StopEntity")
    fun getAllAsFlow(): Flow<List<StopEntity>>

    @Delete
    suspend fun delete(dish: StopEntity)

    @Query("DELETE FROM StopEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM StopEntity")
    suspend fun count(): Int
}