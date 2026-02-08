package hsrm.mi.campusapp.domain.persistence.campus

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CampusDao {

    @Insert
    suspend fun insert(item: CampusEntity)
    @Insert
    suspend fun insertAll(items: List<CampusEntity>)

    @Query("SELECT * FROM CampusEntity WHERE name = :name")
    suspend fun getByName(name: String): CampusEntity?
    @Query("SELECT * FROM CampusEntity")
    fun getAllAsFlow(): Flow<List<CampusEntity>>

    @Delete
    suspend fun delete(dish: CampusEntity)

    @Query("DELETE FROM CampusEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM CampusEntity")
    suspend fun count(): Int

}