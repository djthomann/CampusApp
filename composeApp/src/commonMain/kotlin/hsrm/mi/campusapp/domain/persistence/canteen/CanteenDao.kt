package hsrm.mi.campusapp.domain.persistence.canteen

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CanteenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CanteenEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CanteenEntity>)

    @Query("SELECT * FROM CanteenEntity WHERE name = :name")
    suspend fun getByName(name: String): CanteenEntity?
    @Query("SELECT * FROM CanteenEntity")
    fun getAllAsFlow(): Flow<List<CanteenEntity>>

    @Delete
    suspend fun delete(dish: CanteenEntity)

    @Query("DELETE FROM CanteenEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM CanteenEntity")
    suspend fun count(): Int
}