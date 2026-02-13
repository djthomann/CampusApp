package hsrm.mi.campusapp.data.persistence.building

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BuildingEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BuildingEntity>)

    @Query("SELECT * FROM BuildingEntity WHERE id = :id")
    suspend fun getById(id: String): BuildingEntity?
    @Query("SELECT * FROM BuildingEntity")
    fun getAllAsFlow(): Flow<List<BuildingEntity>>

    @Delete
    suspend fun delete(campus: BuildingEntity)

    @Query("DELETE FROM BuildingEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM BuildingEntity")
    suspend fun count(): Int


}